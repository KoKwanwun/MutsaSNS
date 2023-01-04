package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    Long countByPostId(Long postId);
}
