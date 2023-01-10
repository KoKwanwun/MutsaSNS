package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmDto;
import com.likelion.finalproject.domain.dto.alarm.AlarmType;
import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 좋아요
     */
    public void clickLike(Long postId, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 포스트가 DB에 존재하지 않을 경우
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 이미 좋아요를 눌른 경우
        likeRepository.findByPostIdAndUserId(post.getId(), user.getId())
                .ifPresent(like -> {
                    throw new UserException(ErrorCode.ALREADY_CLICK_LIKE, ErrorCode.ALREADY_CLICK_LIKE.getMessage());
                });

        // 포스트 작성자 DB에 존재하지 않을 경우
        User postUser = userRepository.findByUserName(post.getUserName())
                .orElseThrow(() -> new UserException(ErrorCode.DATABASE_ERROR, "DB에 포스트 작성자가 존재하지 않습니다."));

        // 자기 자신의 포스트에 좋아요를 누르면 알림이 안가도록 설정
        if(!postUser.getId().equals(user.getId())){
            alarmRepository.save(AlarmDto.toEntity(AlarmType.NEW_LIKE_ON_POST, postUser, user.getId(), post.getId()));
        }

        likeRepository.save(Like.builder()
                        .user(user)
                        .post(post)
                        .build());
    }

    public Long countLike(Long postId) {
        // 포스트가 DB에 존재하지 않을 경우
        postRepository.findById(postId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 좋아요 개수 리턴
        return likeRepository.countByPostId(postId);
    }
}
