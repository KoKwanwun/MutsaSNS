package com.likelion.finalproject.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostRegisterRequest {
    private String title;
    private String body;
}
