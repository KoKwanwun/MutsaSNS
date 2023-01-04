package com.likelion.finalproject.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRoleResponse {
    private Long userId;
    private String userName;
    private UserRole preRole;
    private UserRole postRole;
}
