package com.example.feeda.domain.post.dto;

import com.example.feeda.domain.post.entity.PostLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostLikeResponseDTO {

    private final Long id;
    private final Long postId;
    private final Long profileId;
    private final LocalDateTime createdAt;

    public PostLikeResponseDTO(PostLike postLike) {
        this.id = postLike.getId();
        this.postId = postLike.getPost().getId();
        this.profileId = postLike.getProfile().getId();
        this.createdAt = postLike.getCreatedAt();
    }
}
