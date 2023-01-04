package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.domain.dto.user.UserRole;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.CommentRepository;
import com.likelion.finalproject.repository.LikeRepository;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CheckException {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public Post checkPost(Long id){
        // 포스트 존재 X
        return postRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    public User checkUser(String userName){
        // 유저(토큰 인증 받은)가 존재 X(정보가 DB에 없음)
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.DATABASE_ERROR, "DB에 유저가 존재하지 않습니다."));
    }

    public User checkPostUser(String userName){
        // 유저(토큰 인증 받은)가 존재 X(정보가 DB에 없음)
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.DATABASE_ERROR, "DB에 포스트 작성자가 존재하지 않습니다."));
    }

    public Comment checkComment(Long id, Long postId){
        // 댓글이 DB에 존재하지 않을 경우
        return commentRepository.findByIdAndPostId(id, postId)
                .orElseThrow(() -> new UserException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));
    }

    public void checkLike(Long postId, Long userId) {
        likeRepository.findByPostIdAndUserId(postId, userId)
                .ifPresent(like -> {
                    throw new UserException(ErrorCode.ALREADY_CLICK_LIKE, ErrorCode.ALREADY_CLICK_LIKE.getMessage());
                });
    }

    public Post checkEnableChangePost(Long id, String userName) {
        User user = checkUser(userName);
        Post post = checkPost(id);

        // 작성자 != 유저, 하지만 유저의 ROLE이 ADMIN이면 수정이나 삭제가 가능하도록
        if(!post.getUserName().equals(userName) && user.getRole().equals(UserRole.USER)) {
            throw new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        return post;
    }

    public Comment checkEnableChangeComment(Long postId, String userName, Long commentId) {
        User user = checkUser(userName);
        Post post = checkPost(postId);
        Comment comment = checkComment(commentId, post.getId());

        // 작성자 != 유저, 하지만 유저의 ROLE이 ADMIN이면 수정이나 삭제가 가능하도록
        if(!comment.getUser().getUserName().equals(userName) && user.getRole().equals(UserRole.USER)) {
            throw new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        return comment;
    }
}
