package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmDto;
import com.likelion.finalproject.domain.dto.alarm.AlarmType;
import com.likelion.finalproject.domain.dto.comment.CommentDto;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.user.UserRole;
import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 댓글
     */
    public Page<CommentDto> printComment(Long postId, Pageable pageable) {
        // 포스트가 DB에 존재하지 않을 경우
        postRepository.findById(postId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        Page<CommentDto> commentDtos = comments.map(comment -> CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .build());

        return commentDtos;
    }

    public CommentDto createComment(CommentRequest commentRequest, Long postId, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 포스트가 DB에 존재하지 않을 경우
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 포스트 작성자 DB에 존재하지 않을 경우
        User postUser = userRepository.findByUserName(post.getUserName())
                .orElseThrow(() -> new UserException(ErrorCode.DATABASE_ERROR, "DB에 포스트 작성자가 존재하지 않습니다."));

        Comment savedComment = commentRepository.save(CommentDto.toEntity(commentRequest.getComment(), user, post));

        // 자기 자신의 포스트에 댓글을 달면 알림이 안가도록 설정
        if(!postUser.getId().equals(user.getId())) {
            alarmRepository.save(AlarmDto.toEntity(AlarmType.NEW_COMMENT_ON_POST, postUser, user.getId(), post.getId()));
        }

        return CommentDto.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .userName(savedComment.getUser().getUserName())
                .postId(savedComment.getPost().getId())
                .createdAt(savedComment.getCreatedAt())
                .lastModifiedAt(savedComment.getLastModifiedAt())
                .build();
    }

    public CommentDto updateComment(CommentRequest commentRequest, Long postId, Long id, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 포스트가 DB에 존재하지 않을 경우
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 포스트 작성자 DB에 존재하지 않을 경우
        User postUser = userRepository.findByUserName(post.getUserName())
                .orElseThrow(() -> new UserException(ErrorCode.DATABASE_ERROR, "DB에 포스트 작성자가 존재하지 않습니다."));

        // 댓글 수정 가능 여부 체크
        // 댓글이 DB에 존재하지 않을 경우
        Comment comment = commentRepository.findByIdAndPostId(id, post.getId())
                .orElseThrow(() -> new UserException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        // 작성자 != 유저, 하지만 유저의 ROLE이 ADMIN이면 수정이나 삭제가 가능하도록
        if(!comment.getUser().getUserName().equals(userName) && user.getRole().equals(UserRole.USER)) {
            throw new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        comment.setComment(commentRequest.getComment());

        Comment savedComment = commentRepository.save(comment);

        // 자기 자신의 포스트에 댓글을 수정하면 알림이 안가도록 설정
        if(!postUser.getId().equals(user.getId())) {
            alarmRepository.save(AlarmDto.toEntity(AlarmType.UPDATE_COMMENT_ON_POST, postUser, user.getId(), post.getId()));
        }

        return new CommentDto(savedComment.getId(), savedComment.getComment(), savedComment.getUser().getUserName(),
                savedComment.getPost().getId(), savedComment.getCreatedAt(), savedComment.getLastModifiedAt());
    }

    public Long deleteComment(Long postId, Long id, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 포스트가 DB에 존재하지 않을 경우
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 포스트 작성자 DB에 존재하지 않을 경우
        User postUser = userRepository.findByUserName(post.getUserName())
                .orElseThrow(() -> new UserException(ErrorCode.DATABASE_ERROR, "DB에 포스트 작성자가 존재하지 않습니다."));

        // 댓글 수정 가능 여부 체크
        // 댓글이 DB에 존재하지 않을 경우
        Comment comment = commentRepository.findByIdAndPostId(id, post.getId())
                .orElseThrow(() -> new UserException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        // 작성자 != 유저, 하지만 유저의 ROLE이 ADMIN이면 수정이나 삭제가 가능하도록
        if(!comment.getUser().getUserName().equals(userName) && user.getRole().equals(UserRole.USER)) {
            throw new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        commentRepository.delete(comment);

        // 자기 자신의 포스트에 댓글을 수정하면 알림이 안가도록 설정
        if(!postUser.getId().equals(user.getId())) {
            alarmRepository.save(AlarmDto.toEntity(AlarmType.DELETE_COMMENT_ON_POST, postUser, user.getId(), post.getId()));
        }

        return comment.getId();
    }
}
