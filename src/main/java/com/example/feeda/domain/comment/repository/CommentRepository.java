package com.example.feeda.domain.comment.repository;

import com.example.feeda.domain.comment.entity.Comment;
import com.example.feeda.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId); // 최신순

    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    Long countByPost(Post findPost);
}
