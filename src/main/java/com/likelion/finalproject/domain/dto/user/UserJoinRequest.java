package com.likelion.finalproject.domain.dto.user;

import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(String password){
        return User.builder()
                .userName(this.userName)
                .password(password)
                .role(UserRole.USER)
                .build();
    }
}
