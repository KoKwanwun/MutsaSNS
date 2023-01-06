package com.likelion.finalproject.controller.api;

import com.likelion.finalproject.configuration.annotation.Lock;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.comment.CommentDto;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import com.likelion.finalproject.service.CommentService;
import io.swagger.annotations.Api;
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
@Api(tags = "Comment Api")
public class CommentRestController {

    private final CommentService commentService;

    /**
     * 댓글
     */
    @ApiOperation(value = "댓글 조회")
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentDto>> printComment(@PathVariable Long postId, @ApiIgnore @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentDto> commentDtos = commentService.printComment(postId, pageable);
        return Response.success(commentDtos);
    }

    @Lock
    @ApiOperation(value = "댓글 작성")
    @PostMapping("/{postId}/comments")
    public Response<CommentDto> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest, @ApiIgnore Authentication authentication) {
        CommentDto commentDto = commentService.createComment(commentRequest, postId, authentication.getName());
        return Response.success(commentDto);
    }

    @Lock
    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest commentRequest, @ApiIgnore Authentication authentication) {
        CommentDto commentDto = commentService.updateComment(commentRequest, postId, id, authentication.getName());
        return Response.success(commentDto);
    }

    @Lock
    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> deleteComment(@PathVariable Long postId, @PathVariable Long id, @ApiIgnore Authentication authentication) {
        Long commentId = commentService.deleteComment(postId, id, authentication.getName());
        return Response.success(new CommentResponse("댓글 삭제 완료", commentId));
    }
}
