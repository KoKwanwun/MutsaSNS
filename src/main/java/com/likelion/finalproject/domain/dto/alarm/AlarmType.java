package com.likelion.finalproject.domain.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    UPDATE_COMMENT_ON_POST("update comment!"),
    DELETE_COMMENT_ON_POST("delete comment!"),
    NEW_COMMENT_ON_POST("new comment!"),
    NEW_LIKE_ON_POST("new like!")
    ;

    private String text;
}
