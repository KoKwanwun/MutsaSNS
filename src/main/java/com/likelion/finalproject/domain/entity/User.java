package com.likelion.finalproject.domain.entity;

import com.likelion.finalproject.domain.dto.user.UserRole;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Where(clause = "is_deleted = 0")
@SQLDelete(sql = "UPDATE user SET is_deleted = true where id = ?")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
