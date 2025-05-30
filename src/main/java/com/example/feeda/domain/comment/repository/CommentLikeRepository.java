package com.example.feeda.domain.comment.repository;

import com.example.feeda.domain.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByComment_IdAndProfile_Id(Long commentId, Long profileId);
}
