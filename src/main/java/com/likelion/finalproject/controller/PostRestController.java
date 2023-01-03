package com.likelion.finalproject.controller;

import com.likelion.finalproject.configuration.annotation.Lock;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.comment.CommentDto;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
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
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    /**
     * 포스트
     */
    @ApiOperation(value = "포스트 리스트")
    @GetMapping()
    public Response<Page<PostDto>> printPosts(@ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
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
    public Response<PostResponse> createPost(@RequestBody PostRequest postRequest, @ApiIgnore Authentication authentication){
        PostDto postDto = postService.create(postRequest, authentication.getName());
        return Response.success(new PostResponse("포스트 등록 완료", postDto.getId()));
    }

    @Lock
    @ApiOperation(value = "포스트 수정")
    @PutMapping("/{id}")
    public Response<PostResponse> updatePost(@RequestBody PostRequest postRequest, @PathVariable Long id, @ApiIgnore Authentication authentication){
        PostDto postDto = postService.update(id, postRequest, authentication.getName());
        return Response.success(new PostResponse("포스트 수정 완료", postDto.getId()));
    }

    @Lock
    @ApiOperation(value = "포스트 삭제")
    @DeleteMapping("/{id}")
    public Response<PostResponse> deletePost(@PathVariable Long id, @ApiIgnore Authentication authentication){
        PostDto postDto = postService.delete(id, authentication.getName());
        return Response.success(new PostResponse("포스트 삭제 완료", postDto.getId()));
    }

    /**
     * 댓글
     */
    @Lock
    @ApiOperation(value = "댓글 조회")
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentDto>> printComment(@PathVariable Long postId, @ApiIgnore @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication){
        Page<CommentDto> commentDtos = postService.printComment(postId, pageable, authentication.getName());
        return Response.success(commentDtos);
    }

    @Lock
    @ApiOperation(value = "댓글 작성")
    @PostMapping("/{postId}/comments")
    public Response<CommentDto> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest, @ApiIgnore Authentication authentication){
        CommentDto commentDto = postService.createComment(commentRequest, postId, authentication.getName());
        return Response.success(commentDto);
    }

    @Lock
    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest commentRequest, @ApiIgnore Authentication authentication){
        CommentDto commentDto = postService.updateComment(commentRequest, postId, id, authentication.getName());
        return Response.success(commentDto);
    }

    @Lock
    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> deleteComment(@PathVariable Long postId, @PathVariable Long id, @ApiIgnore Authentication authentication){
        Long commentId = postService.deleteComment(postId, id, authentication.getName());
        return Response.success(new CommentResponse("댓글 삭제 완료", commentId));
    }
}
