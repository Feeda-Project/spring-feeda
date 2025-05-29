package com.example.feeda.domain.follow.service;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.dto.ProfilesResponseDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface FollowsService {

    FollowsResponseDto follow(Authentication auth, Long profileId);

    void unfollow(Authentication auth, Long followingId);

    List<ProfilesResponseDto> findFollowings(Long profileId);

    List<ProfilesResponseDto> findFollowers(Long profileId);
}
