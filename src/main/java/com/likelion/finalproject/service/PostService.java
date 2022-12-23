package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.Post;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public PostDto create(PostRequest request, String userName) {
        Post savedPost = postRepository.save(PostDto.toEntity(request.getTitle(), request.getBody(), userName));

        return new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getBody(),
                savedPost.getUserName(), savedPost.getCreatedAt(), savedPost.getLastModifiedAt());
    }

    public PostDto printOnePost(Long postsId) {
        // userName이 없을 경우
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        return new PostDto(post.getId(), post.getTitle(), post.getBody(),
                post.getUserName(), post.getCreatedAt(), post.getLastModifiedAt());
    }

    public Page<PostDto> printPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostDto> postDtos = posts.map(post -> new PostDto(post.getId(),
                post.getTitle(), post.getBody(), post.getUserName(),
                post.getCreatedAt(), post.getLastModifiedAt()));

        return postDtos;
    }

    private Post checkException(Long id, String accessName) {
        // 포스트 존재 X
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        log.info("post.getUserName : " + post.getUserName());
        log.info("accessName : " + accessName);
        log.info("same? : " + (!post.getUserName().equals(accessName)));

        // 수정자가 존재 X(추가해야 함)

        // 작성자 != 수정자 (* 이후, 달라도 ROLE이 ADMIN이라면 수정가능하도록 바꿔야 함)
        if(!post.getUserName().equals(accessName)) {
            throw new UserException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        return post;
    }

    public PostDto update(Long id, PostRequest postRequest, String accessName) {
        Post post = checkException(id, accessName);

        post.setTitle(postRequest.getTitle());
        post.setBody(postRequest.getBody());

        Post savedPost = postRepository.save(post);

        return new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getBody(),
                savedPost.getUserName(), savedPost.getCreatedAt(), savedPost.getLastModifiedAt());
    }

    public PostDto delete(Long id, String accessName) {
        Post post = checkException(id, accessName);

        postRepository.delete(post);

        return new PostDto(post.getId(), post.getTitle(), post.getBody(),
                post.getUserName(), post.getCreatedAt(), post.getLastModifiedAt());
    }
}
