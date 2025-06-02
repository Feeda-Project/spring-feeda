package com.example.feeda.domain.post.repository;

import com.example.feeda.domain.post.entity.Post;
import com.example.feeda.domain.post.entity.PostLike;
import com.example.feeda.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndProfile(Post post, Profile profile);
    long countByPost(Post post); // PostLike 갯수
}
