package com.example.feeda.domain.post.repository;

import com.example.feeda.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByTitleContaining(String title, Pageable pageable);

    Page<Post> findAllByProfile_IdIn(List<Long> followingProfileIds, Pageable pageable);

    Page<Post> findAllByTitleContainingAndUpdatedAtBetween(String title,
        LocalDateTime startUpdatedAt, LocalDateTime endUpdatedAt,
        Pageable pageable);
}
