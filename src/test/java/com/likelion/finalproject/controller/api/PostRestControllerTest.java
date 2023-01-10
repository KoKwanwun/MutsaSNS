package com.likelion.finalproject.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
class PostRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    PostRequest postRequest = PostRequest.builder()
            .title("test title")
            .body("test body")
            .build();

    /**
     * 포스트 상세 테스트
     */
    @Test
    @DisplayName("포스트 상세 조회 성공")
    @WithMockUser
    void printOnePost_success() throws Exception {
        when(postService.printOnePost(any())).thenReturn(PostDto.builder()
                        .id(1L)
                        .title("test title")
                        .body("test body")
                        .userName("test user")
                        .createdAt(String.valueOf(LocalDateTime.now()))
                        .lastModifiedAt(String.valueOf(LocalDateTime.now()))
                        .build());

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 상세 조회 실패 - post 없음")
    void printOnePost_fail() throws Exception {
        when(postService.printOnePost(any())).thenThrow(new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    /**
     * 포스트 등록 테스트
     */
    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void createPost_success() throws Exception {
        when(postService.create(any(), any())).thenReturn(PostDto.builder()
                .id(1L)
                .title("test title")
                .body("test body")
                .userName("test user")
                .createdAt(String.valueOf(LocalDateTime.now()))
                .lastModifiedAt(String.valueOf(LocalDateTime.now()))
                .build());

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 작성 실패 - JWT가 유효하지 않은 경우")
    void createPost_fail1() throws Exception {
        when(postService.create(any(), any())).thenThrow(new UserException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 작성 실패 - JWT 정보 안의 유저가 DB에 없는 경우")
    void createPost_fail2() throws Exception {
        when(postService.create(any(), any())).thenThrow(new UserException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 작성 실패 - 위 2가지 경우를 제외하고 실패하는 경우")
    void createPost_fail3() throws Exception {
        when(postService.create(any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    /**
     * 포스트 수정 테스트
     */
    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void updatePost_success() throws Exception {
        when(postService.update(any(), any(), any())).thenReturn(PostDto.builder()
                .id(1L)
                .title("test title")
                .body("test body")
                .userName("test user")
                .createdAt(String.valueOf(LocalDateTime.now()))
                .lastModifiedAt(String.valueOf(LocalDateTime.now()))
                .build());

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 수정 실패 - 인증 실패")
    void updatePost_fail1() throws Exception {
        when(postService.update(any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 수정 실패 - 작성자 불일치")
    void updatePost_fail2() throws Exception {
        when(postService.update(any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 수정 실패 - 데이터베이스 에러")
    void updatePost_fail3() throws Exception {
        when(postService.update(any(), any(), any())).thenThrow(new UserException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    /**
     * 포스트 삭제 테스트
     */
    @Test
    @DisplayName("포스트 삭제 성공")
    @WithMockUser
    void deletePost_success() throws Exception {
        when(postService.delete(any(), any())).thenReturn(PostDto.builder()
                .id(1L)
                .title("test title")
                .body("test body")
                .userName("test user")
                .createdAt(String.valueOf(LocalDateTime.now()))
                .lastModifiedAt(String.valueOf(LocalDateTime.now()))
                .build());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 실패 - 인증 실패")
    void deletePost_fail1() throws Exception {
        when(postService.delete(any(), any())).thenThrow(new UserException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 실패 - 작성자 불일치")
    void deletePost_fail2() throws Exception {
        when(postService.delete(any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 실패 - 데이터베이스 에러")
    void deletePost_fail3() throws Exception {
        when(postService.delete(any(), any())).thenThrow(new UserException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }


    /**
     * 포스트 리스트 테스트
     */
    @Test
    @DisplayName("포스트 리스트 성공")
    @WithMockUser
    void printPosts_success() throws Exception {
        mockMvc.perform(get("/api/v1/posts")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).printPosts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());
    }

    /**
     * 마이피드 테스트
     */
    @Test
    @DisplayName("마이피드 조회 성공")
    @WithMockUser
    void myFeed_success() throws Exception {
        mockMvc.perform(get("/api/v1/posts/my")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).myFeed(pageableCaptor.capture(), any());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());
    }

    @Test
    @DisplayName("마이피드 조회 실패(1) - 로그인 하지 않은 경우")
    void myFeed_fail() throws Exception {
        when(postService.myFeed(any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }
}