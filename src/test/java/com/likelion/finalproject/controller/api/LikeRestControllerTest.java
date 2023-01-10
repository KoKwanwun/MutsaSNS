package com.likelion.finalproject.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeRestController.class)
class LikeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

    @Test
    @DisplayName("좋아요 누르기 성공")
    @WithMockUser
    void clickLike_success() throws Exception {
        when(likeService.clickLike(any(), any())).thenReturn("좋아요를 눌렀습니다.");

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다."));
    }

    @Test
    @DisplayName("좋아요 누르기 실패(1) - 로그인 하지 않은 경우")
    void clickLike_fail1() throws Exception {
        when(likeService.clickLike(any(), any())).thenThrow(new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @DisplayName("좋아요 누르기 실패(2) - 해당 Post가 없는 경우")
    @WithMockUser
    void clickLike_fail2() throws Exception {
        when(likeService.clickLike(any(), any())).thenThrow(new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @DisplayName("좋아요 개수 성공")
    @WithMockUser
    void countLike_success() throws Exception {
        when(likeService.countLike(any())).thenReturn(1L);

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @DisplayName("좋아요 개수 실패 - 해당 Post가 없는 경우")
    @WithMockUser
    void countLike_fail() throws Exception {
        when(likeService.countLike(any())).thenThrow(new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }
}