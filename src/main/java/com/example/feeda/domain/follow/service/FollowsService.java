package com.example.feeda.domain.follow.service;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.security.jwt.JwtPayload;
import java.util.List;

public interface FollowsService {

    FollowsResponseDto follow(JwtPayload jwtPayload, Long profileId);

    void unfollow(JwtPayload jwtPayload, Long followingId);

    List<GetProfileResponseDto> findFollowings(Long profileId, JwtPayload jwtPayload);

    List<GetProfileResponseDto> findFollowers(Long profileId, JwtPayload jwtPayload);
}
