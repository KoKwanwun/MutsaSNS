package com.likelion.finalproject.domain.dto.comment;

import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    private String comment;

    private String userName;

    private Long postId;

    private String createdAt;
    private String lastModifiedAt;

    public static Comment toEntity(String comment, User user, Post post){
        return Comment.builder()
                .comment(comment)
                .user(user)
                .post(post)
                .build();
    }
}
