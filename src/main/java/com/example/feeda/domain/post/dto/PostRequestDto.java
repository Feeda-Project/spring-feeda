package com.example.feeda.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @NotNull(message = "제목은 필수 항목입니다.")
    private final String title;

    @NotNull(message = "내용은 필수 항목입니다.")
    private final String content;

    @Size(max = 50, message = "카테고리는 50자 이하로 입력하세요.")
    private final String category;

    public PostRequestDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}
