package com.example.feeda.domain.follow.service;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.security.jwt.JwtPayload;
import org.springframework.data.domain.Pageable;

public interface FollowsService {

    FollowsResponseDto follow(JwtPayload jwtPayload, Long profileId);

    void unfollow(JwtPayload jwtPayload, Long followingId);

    ProfileListResponseDto findFollowingsPage(Long profileId, JwtPayload jwtPayload,
        Pageable pageable);

    ProfileListResponseDto findFollowersPage(Long profileId, JwtPayload jwtPayload,
        Pageable pageable);
}
