package com.example.feeda.domain.profile.service;

import com.example.feeda.domain.profile.dto.GetProfileWithFollowCountResponseDto;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.domain.profile.dto.UpdateProfileRequestDto;
import com.example.feeda.domain.profile.dto.UpdateProfileResponseDto;

public interface ProfileService {
    GetProfileWithFollowCountResponseDto getProfile(Long id);

    ProfileListResponseDto getProfiles(String keyword, int page, int size);

    UpdateProfileResponseDto updateProfile(Long userId, Long profileId, UpdateProfileRequestDto requestDto);
}
