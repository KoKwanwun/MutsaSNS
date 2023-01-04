package com.likelion.finalproject.domain.dto.alarm;

import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AlarmDto {
    private Long id;

    private AlarmType alarmType;

    private Long fromUserId;
    private Long targetId;

    private String text;

    private String createdAt;
    private String lastModifiedAt;

    public static Alarm toEntity(AlarmType alarmType, User postUser, Long fromUserId, Long targetId){
        return Alarm.builder()
                .alarmType(alarmType)
                .user(postUser)
                .fromUserId(fromUserId)
                .targetId(targetId)
                .text(alarmType.getText())
                .build();
    }
}
