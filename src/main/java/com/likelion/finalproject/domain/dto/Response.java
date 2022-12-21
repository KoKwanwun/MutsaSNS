package com.likelion.finalproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> error(T errorResult) {
        return new Response<>("ERROR", errorResult);
    }

    public static <T> Response<T> success(T successResult) {
        return new Response<>("SUCCESS", successResult);
    }
}
