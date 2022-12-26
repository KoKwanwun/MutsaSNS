package com.likelion.finalproject.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PostRequest {
    private String title;
    private String body;
}
