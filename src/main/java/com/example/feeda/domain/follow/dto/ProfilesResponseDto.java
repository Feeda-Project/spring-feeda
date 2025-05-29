package com.example.feeda.domain.follow.dto;

import com.example.feeda.domain.follow.entity.Profiles;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * 프로필 응답 Response Dto. 추후 프로필 개발 병합 이후 삭제 예정.
 */

@Getter
public class ProfilesResponseDto {

    private Long id;
    private String nickname;
    private String birth;
    private String bio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    public static ProfilesResponseDto of(Profiles profiles) {
        final ProfilesResponseDto res = new ProfilesResponseDto();

        res.id = profiles.getId();
        res.nickname = profiles.getNickname();
        res.birth = profiles.getBirth();
        res.bio = profiles.getBio();
        res.createdAt = profiles.getCreatedAt();
        res.updatedAt = profiles.getUpdatedAt();

        return res;
    }
}
