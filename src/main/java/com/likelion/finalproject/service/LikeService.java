package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmDto;
import com.likelion.finalproject.domain.dto.alarm.AlarmType;
import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final CheckException checkException;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 좋아요
     */
    public void clickLike(Long postId, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        User user = checkException.checkUser(userName);

        // 포스트가 DB에 존재하지 않을 경우
        Post post = checkException.checkPost(postId);

        // 이미 좋아요를 눌른 경우
        checkException.checkLike(post.getId(), user.getId());

        // 포스트 작성자 DB에 존재하지 않을 경우
        User postUser = checkException.checkPostUser(post.getUserName());

        // 자기 자신의 포스트에 좋아요를 누르면 알림이 안가도록 설정
        if(!postUser.getId().equals(user.getId())){
            alarmRepository.save(AlarmDto.toEntity(AlarmType.NEW_LIKE_ON_POST, postUser, user.getId(), post.getId()));
        }

        likeRepository.save(Like.builder()
                        .user(user)
                        .post(post)
                        .build());
    }

    public Long countLike(Long postId, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        checkException.checkUser(userName);

        // 포스트가 DB에 존재하지 않을 경우
        checkException.checkPost(postId);

        // 좋아요 개수 리턴
        return likeRepository.countByPostId(postId);
    }
}
