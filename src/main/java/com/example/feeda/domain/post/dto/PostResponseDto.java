package com.example.feeda.domain.post.dto;

import com.example.feeda.domain.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private final Long id;

    private final String title;

    private final String content;

    private final String category;

    private final Long likes;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public PostResponseDto(Post post, Long likes) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.likes = likes;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public static PostResponseDto toDto(Post post, Long likes) {
        return new PostResponseDto(post, likes);
    }
}
