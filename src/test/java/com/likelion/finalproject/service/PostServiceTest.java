package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.domain.dto.user.UserRole;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.CommentRepository;
import com.likelion.finalproject.repository.LikeRepository;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PostServiceTest {

    private PostService postService;
    private CheckException checkException;

    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private LikeRepository likeRepository = Mockito.mock(LikeRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(checkException, postRepository, userRepository, commentRepository, likeRepository);
    }

    Long testUserId = 1L;
    Long testPostId = 1L;
    String testUserName = "user";
    String testPassword = "123";
    String testTitle = "title test";
    String testBody = "title body";
    String testAccessUserName = "accessUser";

    User user = User.builder()
            .id(1L)
            .userName("user")
            .password("123")
            .build();

    Post post = Post.builder()
            .id(1L)
            .title("title test")
            .body("title body")
            .userName("user")
            .build();

    User accessUser = User.builder()
            .id(2L)
            .userName("accessUser")
            .password("123")
            .role(UserRole.USER)
            .build();

    // 포스트 상세
    @Test
    @DisplayName("포스트 상세 성공")
    void printOnePost_success() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));

        PostDto postDto = postService.printOnePost(testPostId);
        assertEquals(testUserName, postDto.getUserName());
    }

    // 포스트 등록
    @Test
    @DisplayName("포스트 등록 성공")
    void createPost_success() {
        when(userRepository.findByUserName(testUserName)).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(post);

        Assertions.assertDoesNotThrow(() -> postService.create(new PostRequest(testTitle, testBody), testUserName));
    }

    @Test
    @DisplayName("포스트 등록 실패 - 유저가 존재하지 않음")
    void createPost_fail() {
        when(userRepository.findByUserName(testUserName)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(post);
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.create(new PostRequest(testTitle, testBody), testUserName));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, userException.getErrorCode());
    }

    // 포스트 수정
    @Test
    @DisplayName("포스트 수정 성공")
    void updatePost_success() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testUserName)).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(post);

        Assertions.assertDoesNotThrow(() -> postService.update(testPostId, new PostRequest(testTitle, testBody), testUserName));
    }

    @Test
    @DisplayName("포스트 수정 실패 - 포스트 존재하지 않음")
    void updatePost_fail1() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.empty());
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.update(testPostId, new PostRequest(testTitle, testBody), testAccessUserName));

        assertEquals(ErrorCode.POST_NOT_FOUND, userException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 작성자와 유저가 동일하지 않음")
    void updatePost_fail2() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testAccessUserName)).thenReturn(Optional.of(accessUser));
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.update(testPostId, new PostRequest(testTitle, testBody), testAccessUserName));

        assertEquals(ErrorCode.INVALID_PERMISSION, userException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 유저가 존재하지 않음")
    void updatePost_fail3() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testAccessUserName)).thenReturn(Optional.empty());
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.update(testPostId, new PostRequest(testTitle, testBody), testAccessUserName));

        assertEquals(ErrorCode.DATABASE_ERROR, userException.getErrorCode());
    }

    // 포스트 삭제
    @Test
    @DisplayName("포스트 삭제 성공")
    void deletePost_success() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testUserName)).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> postService.delete(testPostId, testUserName));
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 포스트 존재하지 않음")
    void deletePost_fail1() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.empty());
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.delete(testPostId, testAccessUserName));

        assertEquals(ErrorCode.POST_NOT_FOUND, userException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 작성자와 유저가 동일하지 않음")
    void deletePost_fail2() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testAccessUserName)).thenReturn(Optional.of(accessUser));
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.delete(testPostId, testAccessUserName));

        assertEquals(ErrorCode.INVALID_PERMISSION, userException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 유저가 존재하지 않음")
    void deletePost_fail3() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testAccessUserName)).thenReturn(Optional.empty());
        UserException userException = Assertions.assertThrows(UserException.class, () -> postService.delete(testPostId, testAccessUserName));

        assertEquals(ErrorCode.DATABASE_ERROR, userException.getErrorCode());
    }
}