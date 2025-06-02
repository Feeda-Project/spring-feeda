package com.example.feeda.domain.post.dto;

import com.example.feeda.domain.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long id;

    private final String title;

    private final String content;

    private final String category;

    private final Long likes;

    private final Long comments;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public PostResponseDto(Post post, Long likes, Long comments) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.likes = likes;
        this.comments = comments;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public static PostResponseDto toDto(Post post, Long likes, Long comments) {
        return new PostResponseDto(post, likes, comments);
    }
}
