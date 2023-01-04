package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.Comment;
import com.likelion.finalproject.domain.Like;
import com.likelion.finalproject.domain.Post;
import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.dto.comment.CommentDto;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.repository.CommentRepository;
import com.likelion.finalproject.repository.LikeRepository;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final CheckException checkException;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    /**
     * 포스트
     */
    public PostDto create(PostRequest request, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        checkException.checkUser(userName);

        Post savedPost = postRepository.save(PostDto.toEntity(request.getTitle(), request.getBody(), userName));

        return new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getBody(),
                savedPost.getUserName(), savedPost.getCreatedAt(), savedPost.getLastModifiedAt());
    }

    public PostDto printOnePost(Long postId) {
        // 해당 id의 post가 없을 경우
        Post post = checkException.checkPost(postId);

        return new PostDto(post.getId(), post.getTitle(), post.getBody(),
                post.getUserName(), post.getCreatedAt(), post.getLastModifiedAt());
    }

    public Page<PostDto> printPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostDto> postDtos = posts.map(post -> new PostDto(post.getId(),
                post.getTitle(), post.getBody(), post.getUserName(),
                post.getCreatedAt(), post.getLastModifiedAt()));

        return postDtos;
    }

    public PostDto update(Long id, PostRequest postRequest, String accessName) {
        Post post = checkException.checkEnableChangePost(id, accessName);

        post.setTitle(postRequest.getTitle());
        post.setBody(postRequest.getBody());

        Post savedPost = postRepository.save(post);

        return new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getBody(),
                savedPost.getUserName(), savedPost.getCreatedAt(), savedPost.getLastModifiedAt());
    }

    public PostDto delete(Long id, String accessName) {
        Post post = checkException.checkEnableChangePost(id, accessName);

        postRepository.delete(post);

        return new PostDto(post.getId(), post.getTitle(), post.getBody(),
                post.getUserName(), post.getCreatedAt(), post.getLastModifiedAt());
    }

    /**
     * 댓글
     */
    public Page<CommentDto> printComment(Long postId, Pageable pageable, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        checkException.checkUser(userName);

        // 포스트가 DB에 존재하지 않을 경우
        checkException.checkPost(postId);

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        Page<CommentDto> commentDtos = comments.map(comment -> CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build());

        return commentDtos;
    }

    public CommentDto createComment(CommentRequest commentRequest, Long postId, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        User user = checkException.checkUser(userName);

        // 포스트가 DB에 존재하지 않을 경우
        Post post = checkException.checkPost(postId);

        Comment savedComment = commentRepository.save(CommentDto.toEntity(commentRequest.getComment(), user, post));

        return CommentDto.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .userName(savedComment.getUser().getUserName())
                .postId(savedComment.getPost().getId())
                .createdAt(savedComment.getCreatedAt())
                .build();
    }

    public CommentDto updateComment(CommentRequest commentRequest, Long postId, Long id, String userName) {
        // 댓글 수정 가능 여부 체크
        Comment comment = checkException.checkEnableChangeComment(postId, userName, id);

        comment.setComment(commentRequest.getComment());

        Comment savedComment = commentRepository.save(comment);

        return new CommentDto(savedComment.getId(), savedComment.getComment(), savedComment.getUser().getUserName(),
                savedComment.getPost().getId(), savedComment.getCreatedAt(), savedComment.getLastModifiedAt());
    }

    public Long deleteComment(Long postId, Long id, String userName) {
        // 댓글 수정 가능 여부 체크
        Comment comment = checkException.checkEnableChangeComment(postId, userName, id);

        commentRepository.delete(comment);

        return comment.getId();
    }

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
