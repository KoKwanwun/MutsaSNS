package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.UserRole;
import com.likelion.finalproject.domain.dto.user.UserDto;
import com.likelion.finalproject.domain.dto.user.UserJoinRequest;
import com.likelion.finalproject.domain.dto.user.UserRoleRequest;
import com.likelion.finalproject.domain.dto.user.UserRoleResponse;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.UserRepository;
import com.likelion.finalproject.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expiredTimeMs = 60 * 60 * 1000;  // 1시간

    public UserDto join(UserJoinRequest userJoinRequest) {
        // userName 중복여부 확인
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new UserException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .build();
    }

    public String login(String userName, String password) {
        // userName이 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // password가 올바르지 않은 경우
        if(!encoder.matches(password, user.getPassword())) {
            throw new UserException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return JwtTokenUtil.createToken(userName, secretKey, expiredTimeMs);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public UserRoleResponse roleChange(UserRoleRequest userRoleRequest, Long id, String accessName) {
        // 현재 이 문제는 Token과 연관이 많기 떄문에 Token 에외처리를 끝난 후 처리해야함
        // id 존재 X
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, "해당 id의 유저가 존재하지 않습니다."));
        UserRole preRole = user.getRole();  // 변경 전 role 저장

        // accessName 존재 X (이 경우, 토큰 예외 처리시 그쪽에서 처리되지만 이러한 예외도 있다는 것을 알려주기 위해 작성)
        User accessUser = userRepository.findByUserName(accessName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // accessName의 role이 ADMIN이 아닐 경우
        if(!accessUser.getRole().equals(UserRole.ADMIN)){
            throw new UserException(ErrorCode.INVALID_PERMISSION, "유저의 ROLE이 ADMIN이 아닙니다.");
        }

        // 요청받은 role이 user, admin이 아닌 경우
        if(userRoleRequest.getRole() == null){
            throw new UserException(ErrorCode.ROLE_NOT_FOUND, ErrorCode.ROLE_NOT_FOUND.getMessage());
        }

        // id의 role이 이미 user일 경우(user로 요청됨)
        if(user.getRole().equals(userRoleRequest.getRole()) && user.getRole().equals(UserRole.USER)){
            throw new UserException(ErrorCode.ALREADY_ROLE_USER, ErrorCode.ALREADY_ROLE_USER.getMessage());
        }

        // id의 role이 이미 admin일 경우(admin로 요청됨)
        if(user.getRole().equals(userRoleRequest.getRole()) && user.getRole().equals(UserRole.ADMIN)){
            throw new UserException(ErrorCode.ALREADY_ROLE_ADMIN, ErrorCode.ALREADY_ROLE_ADMIN.getMessage());
        }

        // 위 예외 처리를 모두 통과했다면, 등급 변경하기
        user.setRole(userRoleRequest.getRole());
        User savedUser = userRepository.save(user);

        return new UserRoleResponse(savedUser.getId(), savedUser.getUserName(), preRole, savedUser.getRole());
    }
}
