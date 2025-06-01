package com.example.feeda.domain.comment.dto;

import com.example.feeda.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String profileName;
    private final LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.of(
                comment.getId(),
                comment.getContent(),
                comment.getProfile().getNickname(),
                comment.getCreatedAt()
        );
    }
}
