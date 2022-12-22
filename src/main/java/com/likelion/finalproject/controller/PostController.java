package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRegisterRequest;
import com.likelion.finalproject.domain.dto.post.PostRegisterResponse;
import com.likelion.finalproject.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "포스트 리스트")
    @GetMapping()
    public String printPosts() {
        return "포스트 리스트 출력";
    }

    @ApiOperation(value = "포스트 상세")
    @GetMapping(value = "/{postsId}")
    public Response<PostDto> printOnePost(@PathVariable Long postsId) {
        PostDto postDto = postService.printOnePost(postsId);
        return Response.success(postDto);
    }

    @ApiOperation(value = "포스트 등록")
    @PostMapping()
    public Response<PostRegisterResponse> createPost(@RequestBody PostRegisterRequest postRegisterRequest, Authentication authentication){
        PostDto postDto = postService.create(postRegisterRequest, authentication.getName());
        return Response.success(new PostRegisterResponse("포스트 등록 완료", postDto.getId()));
    }

    @ApiOperation(value = "포스트 수정")
    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id){
        return "Post Update";
    }

    @ApiOperation(value = "포스트 삭제")
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id){
        return "Post Delete";
    }
}
