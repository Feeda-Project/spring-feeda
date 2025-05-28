package com.example.feeda.domain.profile.dto;

import java.util.Date;

public class GetProfileResponseDto {

    private Long accountId;

    private String nickname;

    private Date birth;

    private String bio;

    /**
     * JPA에서 기본으로 사용되는 기본 생성자
     */
    public GetProfileResponseDto() {
    }

    // getter&setter
    public Long getAccountId() {
        return accountId;
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

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
