package com.example.feeda.domain.post.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PostRequestDto {

    private final String title;

    private final String content;

    private final String category;

    public PostRequestDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}
