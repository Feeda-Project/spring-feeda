package com.example.feeda.domain.profile.service;

import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.profile.dto.*;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FollowsRepository followsRepository;

    /**
     * 프로필 단건 조회 기능
     */
    @Transactional(readOnly = true)
    public GetProfileWithFollowCountResponseDto getProfile(Long id) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."
                ));

        Long followerCount = followsRepository.countByFollowings_Id(id);
        Long followingCount = followsRepository.countByFollowers_Id(id);

        return GetProfileWithFollowCountResponseDto.of(
                profile.getId(),
                profile.getNickname(),
                profile.getBirth(),
                profile.getBio(),
                followerCount,
                followingCount,
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    /**
     * 프로필 다건 조회 기능(검색,페이징)
     */

    @Transactional(readOnly = true)
    public ProfileListResponseDto getProfiles(String keyword, int page, int size) {

        if (page < 1 || size < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "페이지 번호는 1 이상, 페이지 크기는 1 이상이어야 합니다."
            );
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        Page<Profile> profilePage;
        if (keyword == null || keyword.trim().isEmpty()) {
            profilePage = profileRepository.findAll(pageable);
        } else {
            profilePage = profileRepository.findByNicknameContaining(keyword, pageable);
        }

        List<GetProfileResponseDto> responseDtoList = profilePage.stream()
                .map(profile -> GetProfileResponseDto.of(
                        profile.getId(),
                        profile.getNickname(),
                        profile.getBirth(),
                        profile.getBio(),
                        profile.getCreatedAt(),
                        profile.getUpdatedAt()
                ))
                .toList();

        return ProfileListResponseDto.of(
                responseDtoList,
                profilePage.getNumber() + 1,  // 다시 1부터 시작하는 번호로 반환
                profilePage.getTotalPages(),
                profilePage.getTotalElements()
        );
    }


    /**
     * 프로필 수정 기능
     */
    @Transactional
    public UpdateProfileResponseDto updateProfile(Long userId, Long profileId, UpdateProfileRequestDto requestDto) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 없음."));

        if (!profile.getAccount().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        if (requestDto.getNickname() != null || requestDto.getBirth() != null || requestDto.getBio() != null) {
            profile.updateProfile(
                    requestDto.getNickname(),
                    requestDto.getBirth(),
                    requestDto.getBio()
            );
        }

        profileRepository.save(profile);

        return UpdateProfileResponseDto.from("프로필이 성공적으로 수정되었습니다.");
    }
}
