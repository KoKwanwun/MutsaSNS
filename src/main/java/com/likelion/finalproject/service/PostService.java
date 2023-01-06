package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.entity.*;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final CheckException checkException;
    private final PostRepository postRepository;

    /**
     * 포스트
     */
    public PostDto create(PostRequest request, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        checkException.checkUser(userName);

        Post savedPost = postRepository.save(PostDto.toEntity(request.getTitle(), request.getBody(), userName));

        return new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getBody(),
                savedPost.getUserName(), savedPost.getCreatedAt(), savedPost.getLastModifiedAt());
    }

    public PostDto printOnePost(Long postId) {
        // 해당 id의 post가 없을 경우
        Post post = checkException.checkPost(postId);

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

    public PostDto update(Long id, PostRequest postRequest, String accessName) {
        Post post = checkException.checkEnableChangePost(id, accessName);

        post.setTitle(postRequest.getTitle());
        post.setBody(postRequest.getBody());

        Post savedPost = postRepository.save(post);

        return new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getBody(),
                savedPost.getUserName(), savedPost.getCreatedAt(), savedPost.getLastModifiedAt());
    }

    public PostDto delete(Long id, String accessName) {
        Post post = checkException.checkEnableChangePost(id, accessName);

        postRepository.delete(post);

        return new PostDto(post.getId(), post.getTitle(), post.getBody(),
                post.getUserName(), post.getCreatedAt(), post.getLastModifiedAt());
    }

    /**
     * 마이피드
     */
    public Page<PostDto> myFeed(Pageable pageable, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        checkException.checkUser(userName);

        Page<Post> posts = postRepository.findByUserName(userName, pageable);
        Page<PostDto> postDtos = posts.map(post -> new PostDto(post.getId(),
                post.getTitle(), post.getBody(), post.getUserName(),
                post.getCreatedAt(), post.getLastModifiedAt()));

        return postDtos;
    }

    /**
     * 포스트 List 리턴
     */
    public List<PostDto> printPostsList() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtos = posts.stream()
                .map(post -> new PostDto(post.getId(),
                        post.getTitle(), post.getBody(), post.getUserName(),
                        post.getCreatedAt(), post.getLastModifiedAt()))
                .collect(Collectors.toList());

        return postDtos;
    }
}
