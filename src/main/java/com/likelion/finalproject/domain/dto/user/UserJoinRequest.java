package com.likelion.finalproject.domain.dto.user;

import com.likelion.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
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
