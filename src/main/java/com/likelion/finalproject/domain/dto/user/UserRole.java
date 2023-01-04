package com.likelion.finalproject.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum UserRole {
    ADMIN, USER;

    @JsonCreator
    public static UserRole create(String requestValue) {
        return Stream.of(values())
                .filter(v -> v.toString().equalsIgnoreCase(requestValue))
                .findFirst()
                .orElse(null);
    }
}
