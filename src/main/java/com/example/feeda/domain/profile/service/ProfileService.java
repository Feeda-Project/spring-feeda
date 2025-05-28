package com.example.feeda.domain.profile.service;

import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * 프로필 단건 조회 기능
     */

    public GetProfileResponseDto getProfileService(Long accountId) {

        Profile profile = profileRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        return new GetProfileResponseDto(
                profile.getAccountId(),
                profile.getNickname(),
                profile.getBirth(),
                profile.getBio()
        );
    }

    /**
     * 프로필 다건 조회 기능(검색,페이징)
     */

    public ProfileListResponseDto getProfilesService(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("accountId").ascending());
        Page<Profile> profilePage;

        if (keyword == null || keyword.isEmpty()) {
            profilePage = profileRepository.findAll(pageable);
        } else {
            profilePage = profileRepository.findByNicknameContaining(keyword, pageable);
        }

        List<GetProfileResponseDto> responseDtoList = profilePage.stream()
                .map(profile -> new GetProfileResponseDto(
                        profile.getAccountId(),
                        profile.getNickname(),
                        profile.getBirth(),
                        profile.getBio()
                ))
                .toList();

        return new ProfileListResponseDto(
                responseDtoList,
                profilePage.getNumber(),
                profilePage.getTotalPages(),
                profilePage.getTotalElements()
        );
    }


}
