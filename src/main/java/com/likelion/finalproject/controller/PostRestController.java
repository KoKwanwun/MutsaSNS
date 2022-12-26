package com.likelion.finalproject.controller;

import com.likelion.finalproject.configuration.annotation.Lock;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.domain.dto.post.PostResponse;
import com.likelion.finalproject.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @ApiOperation(value = "포스트 리스트")
    @GetMapping()
    public Response<Page<PostDto>> printPosts(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.printPosts(pageable);
        return Response.success(posts);
    }

    @ApiOperation(value = "포스트 상세")
    @GetMapping(value = "/{postsId}")
    public Response<PostDto> printOnePost(@PathVariable Long postsId) {
        PostDto postDto = postService.printOnePost(postsId);
        return Response.success(postDto);
    }

    @Lock
    @ApiOperation(value = "포스트 등록")
    @PostMapping()
    public Response<PostResponse> createPost(@RequestBody PostRequest postRequest, Authentication authentication){
        PostDto postDto = postService.create(postRequest, authentication.getName());
        return Response.success(new PostResponse("포스트 등록 완료", postDto.getId()));
    }

    @Lock
    @ApiOperation(value = "포스트 수정")
    @PutMapping("/{id}")
    public Response<PostResponse> updatePost(@RequestBody PostRequest postRequest, @PathVariable Long id, Authentication authentication){
        PostDto postDto = postService.update(id, postRequest, authentication.getName());
        return Response.success(new PostResponse("포스트 수정 완료", postDto.getId()));
    }

    @Lock
    @ApiOperation(value = "포스트 삭제")
    @DeleteMapping("/{id}")
    public Response<PostResponse> deletePost(@PathVariable Long id, Authentication authentication){
        PostDto postDto = postService.delete(id, authentication.getName());
        return Response.success(new PostResponse("포스트 삭제 완료", postDto.getId()));
    }
}
