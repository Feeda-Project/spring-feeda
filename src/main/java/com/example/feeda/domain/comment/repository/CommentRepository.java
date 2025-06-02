package com.example.feeda.domain.comment.repository;

import com.example.feeda.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId); // 최신순

    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}
