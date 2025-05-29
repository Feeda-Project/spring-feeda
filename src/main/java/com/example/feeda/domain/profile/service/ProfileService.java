package com.example.feeda.domain.profile.service;

import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.domain.profile.dto.UpdateProfileRequestDto;
import com.example.feeda.domain.profile.dto.UpdateProfileResponseDto;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
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
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * 프로필 단건 조회 기능
     */
    @Transactional(readOnly = true)
    public GetProfileResponseDto getProfile(Long id) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."
                ));

        return new GetProfileResponseDto(
                profile.getId(),
                profile.getNickname(),
                profile.getBirth(),
                profile.getBio(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    /**
     * 프로필 다건 조회 기능(검색,페이징)
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ProfileListResponseDto getProfiles(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Profile> profilePage;

        if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "요청 파라미터가 잘못되었습니다. (예: page < 0, size <= 0)"
            );
        }

        if (keyword == null || keyword.isEmpty()) {
            profilePage = profileRepository.findAll(pageable);
        } else {
            profilePage = profileRepository.findByNicknameContaining(keyword, pageable);
        }


        List<GetProfileResponseDto> responseDtoList = profilePage.stream()
                .map(profile -> new GetProfileResponseDto(
                        profile.getId(),
                        profile.getNickname(),
                        profile.getBirth(),
                        profile.getBio(),
                        profile.getCreatedAt(),
                        profile.getUpdatedAt()
                ))
                .toList();

        return new ProfileListResponseDto(
                responseDtoList,
                profilePage.getNumber(),
                profilePage.getTotalPages(),
                profilePage.getTotalElements()
        );
    }

    /**
     * 프로필 수정 기능
     */
    @Transactional
    public UpdateProfileResponseDto updateProfile(Long id, UpdateProfileRequestDto requestDto) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 없음."));

        profile.updateProfile(
                requestDto.getNickname(),
                requestDto.getBirth(),
                requestDto.getBio()
        );

        profileRepository.save(profile);

        return new UpdateProfileResponseDto("프로필이 성공적으로 수정되었습니다.");
    }
}
