package com.likelion.finalproject.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.comment.CommentDto;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.service.CommentService;
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

@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Autowired
    ObjectMapper objectMapper;

    CommentRequest commentRequest = CommentRequest.builder()
            .comment("test comment")
            .build();

    /**
     * ?????? ?????? ?????????
     */
    @Test
    @DisplayName("?????? ?????? ??????")
    @WithMockUser
    void createComment_success() throws Exception {
        when(commentService.createComment(any(), any(), any())).thenReturn(CommentDto.builder()
                .id(1L)
                .comment("test comment")
                .userName("test user")
                .postId(1L)
                .createdAt(String.valueOf(LocalDateTime.now()))
                .lastModifiedAt(String.valueOf(LocalDateTime.now()))
                .build());

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").value("test comment"))
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());
    }

    @Test
    @DisplayName("?????? ?????? ??????(1) - ????????? ?????? ?????? ??????")
    void createComment_fail1() throws Exception {
        when(commentService.createComment(any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(2) - ???????????? ???????????? ?????? ??????")
    @WithMockUser
    void createComment_fail2() throws Exception {
        when(commentService.createComment(any(), any(), any())).thenThrow(new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    /**
     * ?????? ?????? ?????????
     */
    @Test
    @DisplayName("?????? ?????? ??????")
    @WithMockUser
    void updateComment_success() throws Exception {
        when(commentService.updateComment(any(), any(), any(), any())).thenReturn(CommentDto.builder()
                .id(1L)
                .comment("test update comment")
                .userName("test user")
                .postId(1L)
                .createdAt(String.valueOf(LocalDateTime.now()))
                .lastModifiedAt(String.valueOf(LocalDateTime.now()))
                .build());

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").value("test update comment"))
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());
    }

    @Test
    @DisplayName("?????? ?????? ??????(1) : ?????? ??????")
    void updateComment_fail1() throws Exception {
        when(commentService.updateComment(any(), any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(2) : Post ?????? ??????")
    @WithMockUser
    void updateComment_fail2() throws Exception {
        when(commentService.updateComment(any(), any(), any(), any())).thenThrow(new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(3) : ????????? ?????????")
    @WithMockUser
    void updateComment_fail3() throws Exception {
        when(commentService.updateComment(any(), any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(4) : ?????????????????? ??????")
    @WithMockUser
    void updateComment_fail4() throws Exception {
        when(commentService.updateComment(any(), any(), any(), any())).thenThrow(new UserException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    /**
     * ?????? ?????? ?????????
     */
    @Test
    @DisplayName("?????? ?????? ??????")
    @WithMockUser
    void deleteComment_success() throws Exception {
        when(commentService.deleteComment(any(), any(), any())).thenReturn(1L);

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").value("?????? ?????? ??????"))
                .andExpect(jsonPath("$.result.id").value(1L));
    }

    @Test
    @DisplayName("?????? ?????? ??????(1) : ?????? ??????")
    void deleteComment_fail1() throws Exception {
        when(commentService.deleteComment(any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(2) : Post?????? ??????")
    @WithMockUser
    void deleteComment_fail2() throws Exception {
        when(commentService.deleteComment(any(), any(), any())).thenThrow(new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(3) : ????????? ?????????")
    void deleteComment_fail3() throws Exception {
        when(commentService.deleteComment(any(), any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @DisplayName("?????? ?????? ??????(4) : ?????????????????? ??????")
    @WithMockUser
    void deleteComment_fail4() throws Exception {
        when(commentService.deleteComment(any(), any(), any())).thenThrow(new UserException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    /**
     * ?????? ????????? ?????????
     */
    @Test
    @DisplayName("?????? ????????? ??????")
    @WithMockUser
    void printComment_success() throws Exception {
        mockMvc.perform(get("/api/v1/posts/1/comments")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(commentService).printComment(any(), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());
    }
}