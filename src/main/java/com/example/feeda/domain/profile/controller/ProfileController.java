package com.example.feeda.domain.profile.controller;

import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.domain.profile.dto.UpdateProfileRequestDto;
import com.example.feeda.domain.profile.dto.UpdateProfileResponseDto;
import com.example.feeda.domain.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * 프로필 단건 조회 API
     */

    @GetMapping("/profiles/{accountId}")
    public ResponseEntity<GetProfileResponseDto> getProfileAPI(@PathVariable Long accountId) {
        GetProfileResponseDto responseDto = profileService.getProfileService(accountId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    /**
     * 프로필 전체 조회 API (검색, 페이징)
     */

    @GetMapping("/profiles")
    public ResponseEntity<ProfileListResponseDto> getProfilesAPI(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProfileListResponseDto response = profileService.getProfilesService(keyword, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 프로필 전체 수정 API (검색, 페이징)
     */

    @PatchMapping("/profiles/{accountId}")
    public ResponseEntity<UpdateProfileResponseDto> patchProfile(
            @PathVariable Long accountId,
            @RequestBody UpdateProfileRequestDto requestDto) {

        UpdateProfileResponseDto responseDto = profileService.updateProfile(accountId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
