package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.dto.UserDto;
import com.likelion.finalproject.domain.dto.UserJoinRequest;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest userJoinRequest) {
        // userName 중복여부 확인
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new UserException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        User savedUser = userRepository.save(userJoinRequest.toEntity());

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .build();
    }
}
