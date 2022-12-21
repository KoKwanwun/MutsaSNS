package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.post.PostRegisterRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @ApiOperation(value = "포스트 리스트")
    @GetMapping()
    public String printPosts() {
        return "";
    }

    @ApiOperation(value = "포스트 상세")
    @GetMapping(value = "/{postsId}")
    public String printOnePost(@PathVariable Long postsId) {
        return "";
    }

    @ApiOperation(value = "포스트 등록")
    @PostMapping
    public String registerPost(@RequestBody PostRegisterRequest postRegisterRequest){
        return "Post Register";
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
