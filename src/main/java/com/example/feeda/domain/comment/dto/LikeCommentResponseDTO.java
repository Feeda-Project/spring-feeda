package com.example.feeda.domain.comment.dto;

import com.example.feeda.domain.comment.entity.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LikeCommentResponseDTO {
    private final Long id;
    private final Long commentId;
    private final Long profileId;
    private final LocalDateTime createdAt;

    public LikeCommentResponseDTO(CommentLike commentLike) {
        this.id = commentLike.getId();
        this.commentId = commentLike.getComment().getId();
        this.profileId = commentLike.getProfile().getId();
        this.createdAt = commentLike.getCreatedAt();
    }
}
