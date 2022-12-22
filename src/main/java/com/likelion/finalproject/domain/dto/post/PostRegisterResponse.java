package com.likelion.finalproject.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostRegisterResponse {
    private String message;
    private Long postId;
}
