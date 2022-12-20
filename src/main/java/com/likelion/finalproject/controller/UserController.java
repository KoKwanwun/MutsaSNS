package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.*;
import com.likelion.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getId(), userDto.getUserName()));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        return Response.success(new UserLoginResponse());
    }
}
