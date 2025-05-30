package com.example.feeda.domain.profile.dto;

import lombok.Getter;

@Getter
public class UpdateProfileResponseDto {

    private final String message;

    private UpdateProfileResponseDto(String message) {
        this.message = message;
    }

    public static UpdateProfileResponseDto from(String message) {
        return new UpdateProfileResponseDto(message);
    }
}
