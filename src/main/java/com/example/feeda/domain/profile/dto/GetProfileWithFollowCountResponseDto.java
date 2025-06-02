package com.example.feeda.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor(staticName = "of")
@Getter
public class GetProfileWithFollowCountResponseDto {

    // 계정 ID
    private final Long id;

    // 닉네임
    private final String nickname;

    // 생일
    private final Date birth;

    // 자기소개
    private final String bio;

    // 팔로워 수
    private final Long followerCount;

    // 팔로잉 수
    private final Long followingCount;

    //생성 시간
    private final LocalDateTime createdAt;

    //마지막 수정 시간
    private final LocalDateTime updatedAt;
}

