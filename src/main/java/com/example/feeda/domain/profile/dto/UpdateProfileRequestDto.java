package com.example.feeda.domain.profile.dto;

import java.util.Date;

public class UpdateProfileRequestDto {

    // 닉네임
    private final String nickname;

    private final Date birth;

    // 자기소개
    private final String bio;

    /**
     * 모든 필드를 초기화하는 생성자
     */

    public UpdateProfileRequestDto(String nickname, Date birth, String bio) {
        this.nickname = nickname;
        this.birth = birth;
        this.bio = bio;
    }

    // getter 메서드들

    public Date getBirth() {
        return birth;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBio() {
        return bio;
    }
}
