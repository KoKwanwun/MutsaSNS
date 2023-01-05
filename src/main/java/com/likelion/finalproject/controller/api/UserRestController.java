package com.likelion.finalproject.controller.api;

import com.likelion.finalproject.configuration.annotation.Lock;
import com.likelion.finalproject.domain.dto.*;
import com.likelion.finalproject.domain.dto.user.*;
import com.likelion.finalproject.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Api(tags = "User Api")
public class UserRestController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getId(), userDto.getUserName()));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        String token = userService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @Lock
    @ApiOperation(value = "등급 Change - ADMIN만 가능( {”role”:”ADMIN”} or {”role”:”USER”} )")
    @PostMapping("/{id}/role/change")
    public Response<UserRoleResponse> roleChange(@RequestBody UserRoleRequest userRoleRequest, @PathVariable Long id, Authentication authentication){
        UserRoleResponse userRoleResponse = userService.roleChange(userRoleRequest, id, authentication.getName());
        return Response.success(userRoleResponse);
    }
}
