package com.example.feeda.domain.profile.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class GetProfileResponseDto {

    // 계정 ID
    private final Long id;

    // 닉네임
    private final String nickname;

    // 생일
    private final Date birth;

    // 자기소개
    private final String bio;

    //생성 시간
    private final LocalDateTime createdAt;

    //마지막 수정 시간
    private final LocalDateTime updatedAt;

    /**
     * 모든 필드를 초기화하는 생성자
     */

    public GetProfileResponseDto(Long id, String nickname, Date birth, String bio,LocalDateTime createdAt,LocalDateTime updatedAt) {
        this.id = id;
        this.nickname = nickname;
        this.birth = birth;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * getter 메서드들
     */

    public Long getAccountId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Date getBirth() {
        return birth;
    }

    public String getBio() {
        return bio;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

