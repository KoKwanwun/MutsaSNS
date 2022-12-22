package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.Post;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostDto create(PostRequest request, String userName) {
        Post savedPost = postRepository.save(PostDto.toEntity(request.getTitle(), request.getBody(), userName));

        return PostDto.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .userName(savedPost.getUserName())
                .createdAt(savedPost.getCreatedAt())
                .lastModifiedAt(savedPost.getLastModifiedAt())
                .build();
    }

    public PostDto printOnePost(Long postsId) {
        // userName이 없을 경우
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new UserException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUserName())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    public Page<PostDto> printPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostDto> postDtos = posts.map(post -> new PostDto(post.getId(),
                post.getTitle(), post.getBody(), post.getUserName(),
                post.getCreatedAt(), post.getLastModifiedAt()));

        return postDtos;
    }
}
