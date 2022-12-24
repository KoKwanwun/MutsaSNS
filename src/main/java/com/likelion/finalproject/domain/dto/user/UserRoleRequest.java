package com.likelion.finalproject.domain.dto.user;

import com.likelion.finalproject.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRoleRequest {
    private UserRole role;
}
