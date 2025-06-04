package com.example.feeda.domain.profile.controller;

import com.example.feeda.domain.profile.dto.*;
import com.example.feeda.domain.profile.service.ProfileServiceImpl;
import com.example.feeda.security.jwt.JwtPayload;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final ProfileServiceImpl profileService;

    public ProfileController(ProfileServiceImpl profileService) {
        this.profileService = profileService;
    }

    /**
     * 프로필 단건 조회 API
     */

    @GetMapping("/profiles/{id}")
    public ResponseEntity<GetProfileWithFollowCountResponseDto> getProfile(@PathVariable Long id) {
        GetProfileWithFollowCountResponseDto responseDto = profileService.getProfile(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 프로필 전체 조회 API (검색, 페이징)
     */

    @GetMapping("/profiles")
    public ResponseEntity<ProfileListResponseDto> getProfiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProfileListResponseDto response = profileService.getProfiles(keyword, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 프로필 전체 수정 API (검색, 페이징)
     */

    @PutMapping("/profiles/{id}")
    public ResponseEntity<UpdateProfileResponseDto> updateProfile(
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequestDto requestDto) {

        Long accountId = jwtPayload.getAccountId();
        UpdateProfileResponseDto responseDto = profileService.updateProfile(accountId, id, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
