package com.likelion.finalproject.domain.dto.user;

import com.likelion.finalproject.domain.UserRole;
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
