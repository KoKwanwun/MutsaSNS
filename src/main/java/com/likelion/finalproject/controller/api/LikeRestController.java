package com.likelion.finalproject.controller.api;

import com.likelion.finalproject.configuration.annotation.Lock;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = "Like Api")
public class LikeRestController {

    private final LikeService likeService;

    /**
     * 좋아요
     */
    @Lock
    @ApiOperation(value = "좋아요 누르기")
    @PostMapping("/{postId}/likes")
    public Response<String> clickLike(@PathVariable Long postId, @ApiIgnore Authentication authentication) {
        String result = likeService.clickLike(postId, authentication.getName());
        return Response.success(result);
    }

    @ApiOperation(value = "좋아요 개수")
    @GetMapping("/{postId}/likes")
    public Response<Long> countLike(@PathVariable Long postId) {
        Long cntLike = likeService.countLike(postId);
        return Response.success(cntLike);
    }
}
