package com.example.feeda.domain.profile.dto;

public class UpdateProfileResponseDto {

    private String message;

    public UpdateProfileResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
