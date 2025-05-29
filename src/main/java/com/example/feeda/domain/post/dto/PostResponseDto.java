package com.example.feeda.domain.post.dto;

import com.example.feeda.domain.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long id;

    private final String title;

    private final String content;

    private final String category;

    public PostResponseDto(Long id, String title, String content, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public static PostResponseDto toDto(Post post) {
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getCategory());
    }
}
