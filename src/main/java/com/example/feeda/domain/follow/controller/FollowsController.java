package com.example.feeda.domain.follow.controller;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.service.FollowsService;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.security.jwt.JwtPayload;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowsController {

    private final FollowsService followsService;

    @PostMapping("/{profileId}")
    public FollowsResponseDto follow(@PathVariable Long profileId,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        return followsService.follow(jwtPayload, profileId);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Object> unfollow(@PathVariable Long followingId,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        followsService.unfollow(jwtPayload, followingId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{profileId}/followings")
    public ProfileListResponseDto getFollowings(@PathVariable Long profileId,
        @RequestParam(defaultValue = "1") @Min(1) Integer page,
        @RequestParam(defaultValue = "10") @Min(5) Integer size,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        Pageable pageable = PageRequest
            .of(page - 1, size, Direction.DESC, "createdAt");

        return followsService.findFollowingsPage(profileId, jwtPayload, pageable);
    }

    @GetMapping("/{profileId}/followers")
    public ProfileListResponseDto getFollowers(@PathVariable Long profileId,
        @RequestParam(defaultValue = "1") @Min(1) Integer page,
        @RequestParam(defaultValue = "10") @Min(5) Integer size,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        Pageable pageable = PageRequest
            .of(page - 1, size, Direction.DESC, "createdAt");

        return followsService.findFollowersPage(profileId, jwtPayload, pageable);
    }

    @GetMapping("/followings")
    public ProfileListResponseDto getMyFollowings(
        @AuthenticationPrincipal JwtPayload jwtPayload,
        @RequestParam(defaultValue = "1") @Min(1) Integer page,
        @RequestParam(defaultValue = "10") @Min(5) Integer size) {

        Pageable pageable = PageRequest
            .of(page - 1, size, Direction.DESC, "createdAt");

        return followsService.findFollowingsPage(jwtPayload.getProfileId(), jwtPayload, pageable);
    }

    @GetMapping("/followers")
    public ProfileListResponseDto getMyFollowers(
        @AuthenticationPrincipal JwtPayload jwtPayload,
        @RequestParam(defaultValue = "1") @Min(1) Integer page,
        @RequestParam(defaultValue = "10") @Min(5) Integer size) {

        Pageable pageable = PageRequest
            .of(page - 1, size, Direction.DESC, "createdAt");

        return followsService.findFollowersPage(jwtPayload.getProfileId(), jwtPayload, pageable);
    }
}
