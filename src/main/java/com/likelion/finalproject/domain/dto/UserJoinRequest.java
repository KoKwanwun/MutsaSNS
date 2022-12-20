package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(){
        return User.builder()
                .userName(this.userName)
                .password(this.password)
                .role(UserRole.USER)
                .build();
    }
}
