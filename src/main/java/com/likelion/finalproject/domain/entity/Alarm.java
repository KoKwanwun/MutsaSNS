package com.likelion.finalproject.domain.entity;

import com.likelion.finalproject.domain.dto.alarm.AlarmType;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Where(clause = "is_deleted = 0")
@SQLDelete(sql = "UPDATE alarm SET is_deleted = true where id = ?")
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long fromUserId;
    private Long targetId;

    private String text;
}
