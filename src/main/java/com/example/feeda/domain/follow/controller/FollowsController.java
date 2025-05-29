package com.example.feeda.domain.follow.controller;

import com.example.feeda.domain.follow.dto.FollowResponse;
import com.example.feeda.domain.follow.dto.ProfileResponse;
import com.example.feeda.domain.follow.service.FollowsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public FollowResponse follow(@PathVariable Long profileId,
        Authentication auth) {

        return followsService.follow(auth, profileId);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Object> unfollow(@PathVariable Long followingId,
        Authentication auth) {

        followsService.unfollow(auth, followingId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{profileId}/followings")
    public List<ProfileResponse> getFollowings(@PathVariable Long profileId) {

        return followsService.findFollowings(profileId);
    }

    @GetMapping("/{profileId}/followers")
    public List<ProfileResponse> getFollowers(@PathVariable Long profileId) {

        return followsService.findFollowers(profileId);
    }

    @GetMapping("/followings")
    public List<ProfileResponse> getMyFollowings(Authentication auth) {

        return followsService.findFollowings(Long.parseLong(auth.getName()));
    }

    @GetMapping("/followers")
    public List<ProfileResponse> getMyFollowers(Authentication auth) {

        return followsService.findFollowers(Long.parseLong(auth.getName()));
    }
}
