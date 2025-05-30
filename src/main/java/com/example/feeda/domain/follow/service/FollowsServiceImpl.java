package com.example.feeda.domain.follow.service;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.security.jwt.JwtPayload;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowsServiceImpl implements FollowsService {

    private final FollowsRepository followsRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public FollowsResponseDto follow(JwtPayload jwtPayload, Long profileId) {

        Profile myProfile = getProfileOrThrow(jwtPayload.getProfileId());
        validateNotSelf(myProfile, profileId);
        Profile followingProfile = getProfileOrThrow(profileId);

        Optional<Follows> follow =
            followsRepository.findByFollowersAndFollowings(myProfile, followingProfile);
        if (follow.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "이미 팔로우한 계정입니다.");
        }

        Follows newFollow = Follows.builder()
            .followers(myProfile)
            .followings(followingProfile)
            .build();

        followsRepository.save(newFollow);

        return FollowsResponseDto.of(newFollow);
    }


    @Override
    @Transactional
    public void unfollow(JwtPayload jwtPayload, Long followingId) {

        Profile myProfile = getProfileOrThrow(jwtPayload.getProfileId());
        Profile followingProfile = getProfileOrThrow(followingId);
        validateNotSelf(myProfile, followingId);

        Optional<Follows> follows =
            followsRepository.findByFollowersAndFollowings(myProfile, followingProfile);
        if (follows.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "존재하지 않는 팔로우입니다.");
        }

        followsRepository.delete(follows.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetProfileResponseDto> findFollowings(Long profileId, JwtPayload jwtPayload) {

        List<Follows> followings = followsRepository.findAllByFollowers_Id(profileId);
        List<Profile> profiles = followings.stream()
            .map(Follows::getFollowings)
            .toList();

        return profiles.stream()
            .map(profile -> GetProfileResponseDto.of(
                profile.getId(),
                profile.getNickname(),
                profile.getBirth(),
                profile.getBio(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
            ))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetProfileResponseDto> findFollowers(Long profileId, JwtPayload jwtPayload) {

        List<Follows> followers = followsRepository.findAllByFollowings_Id(profileId);
        List<Profile> profiles = followers.stream()
            .map(Follows::getFollowers)
            .toList();

        return profiles.stream()
            .map(profile -> GetProfileResponseDto.of(
                profile.getId(),
                profile.getNickname(),
                profile.getBirth(),
                profile.getBio(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
            ))
            .toList();
    }

    private Profile getProfileOrThrow(Long profileId) {
        Optional<Profile> optionalProfile =
            profileRepository.findById(profileId);

        if (optionalProfile.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다. id = " + profileId);
        }

        return optionalProfile.get();
    }

    private void validateNotSelf(Profile me, Long profileId) {
        if (me.getId().equals(profileId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "본인 프로필은 팔로우/언팔로우 할 수 없습니다");
        }
    }
}
