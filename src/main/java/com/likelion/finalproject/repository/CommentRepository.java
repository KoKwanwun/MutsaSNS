package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postsId, Pageable pageable);
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
}
