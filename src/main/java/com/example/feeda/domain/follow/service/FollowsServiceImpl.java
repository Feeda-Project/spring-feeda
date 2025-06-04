package com.example.feeda.domain.follow.service;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.profile.dto.GetProfileResponseDto;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.exception.CustomResponseException;
import com.example.feeda.exception.enums.ResponseError;
import com.example.feeda.security.jwt.JwtPayload;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CustomResponseException(ResponseError.ALREADY_FOLLOWED);
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
            throw new CustomResponseException(ResponseError.FOLLOW_NOT_FOUND);
        }

        followsRepository.delete(follows.get());
    }

    @Override
    public ProfileListResponseDto findFollowingsPage(
        Long profileId,
        JwtPayload jwtPayload,
        Pageable pageable) {

        Page<Profile> profiles = followsRepository.findAllByFollowers_Id(profileId, pageable).map(
            Follows::getFollowings);

        List<GetProfileResponseDto> responseDtoList = profiles.stream()
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
            profiles.getNumber() + 1,  // 다시 1부터 시작하는 번호로 반환
            profiles.getTotalPages(),
            profiles.getTotalElements()
        );
    }

    @Override
    public ProfileListResponseDto findFollowersPage(
        Long profileId,
        JwtPayload jwtPayload,
        Pageable pageable) {

        Page<Profile> profiles = followsRepository.findAllByFollowings_Id(profileId, pageable)
            .map(Follows::getFollowers);

        List<GetProfileResponseDto> responseDtoList = profiles.stream()
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
            profiles.getNumber() + 1,  // 다시 1부터 시작하는 번호로 반환
            profiles.getTotalPages(),
            profiles.getTotalElements()
        );
    }

    private Profile getProfileOrThrow(Long profileId) {
        Optional<Profile> optionalProfile =
            profileRepository.findById(profileId);

        if (optionalProfile.isEmpty()) {
            throw new CustomResponseException(ResponseError.PROFILE_NOT_FOUND);
        }

        return optionalProfile.get();
    }

    private void validateNotSelf(Profile me, Long profileId) {
        if (me.getId().equals(profileId)) {
            throw new CustomResponseException(ResponseError.CANNOT_FOLLOW_SELF);
        }
    }
}
