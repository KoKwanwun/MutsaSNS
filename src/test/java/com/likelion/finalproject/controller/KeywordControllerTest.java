package com.likelion.finalproject.controller;

import com.likelion.finalproject.service.AlgorithmService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KeywordController.class)
class KeywordControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlgorithmService algorithmService;

    @Test
    @WithMockUser
    @DisplayName("keyword가 잘 나오는지")
    void keyword() throws Exception {
        mockMvc.perform(get("/api/v1/hello")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("고관운"));
    }

    @Test
    @WithMockUser
    @DisplayName("자릿수 합 구하기")
    void sumOfDigit() throws Exception {
        when(algorithmService.sumOfDigit(any())).thenReturn(21);

        mockMvc.perform(get("/api/v1/hello/687")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("21"));
    }
}