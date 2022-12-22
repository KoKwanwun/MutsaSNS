package com.likelion.finalproject.domain.dto.post;

import com.likelion.finalproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String body;
    private String userName;

    private String createdAt;
    private String lastModifiedAt;

    public static Post toEntity(String title, String body, String userName){
        return Post.builder()
                .title(title)
                .body(body)
                .userName(userName)
                .build();
    }
}
