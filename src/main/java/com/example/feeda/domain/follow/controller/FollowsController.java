package com.example.feeda.domain.follow.controller;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.service.FollowsService;
import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.security.jwt.JwtPayload;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<GetProfileResponseDto> getFollowings(@PathVariable Long profileId,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        return followsService.findFollowings(profileId, jwtPayload);
    }

    @GetMapping("/{profileId}/followers")
    public List<GetProfileResponseDto> getFollowers(@PathVariable Long profileId,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        return followsService.findFollowers(profileId, jwtPayload);
    }

    @GetMapping("/followings")
    public List<GetProfileResponseDto> getMyFollowings(
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        return followsService.findFollowings(jwtPayload.getProfileId(), jwtPayload);
    }

    @GetMapping("/followers")
    public List<GetProfileResponseDto> getMyFollowers(
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        return followsService.findFollowers(jwtPayload.getProfileId(), jwtPayload);
    }
}
