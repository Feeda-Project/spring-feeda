package com.example.feeda.domain.follow.dto;

import com.example.feeda.domain.follow.entity.Follows;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class FollowsResponseDto {

    private Long id;
    private Long followerId;
    private Long followingId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static FollowsResponseDto of(Follows follows) {
        final FollowsResponseDto res = new FollowsResponseDto();

        res.id = follows.getId();
        res.followerId = follows.getFollowers().getId();
        res.followingId = follows.getFollowings().getId();
        res.createdAt = follows.getCreatedAt();

        return res;
    }
}
