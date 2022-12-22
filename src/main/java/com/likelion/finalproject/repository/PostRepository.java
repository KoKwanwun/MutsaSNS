package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
