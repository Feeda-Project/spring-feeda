package com.example.feeda.domain.follow.service;

import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.dto.ProfilesResponseDto;
import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.follow.entity.Profiles;
import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.follow.repository.ProfilesRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowsServiceImpl implements FollowsService {

    private final FollowsRepository followsRepository;
    private final ProfilesRepository profilesRepository;

    @Override
    @Transactional
    public FollowsResponseDto follow(Authentication auth, Long profileId) {

        Long myProfileId = Long.parseLong(auth.getName());

        Optional<Profiles> myProfile =
            profilesRepository.findById(myProfileId);
        if (myProfile.isEmpty()) {
            log.warn("존재하지 않는 유저 정보입니다.");
            return null;
        }

        Optional<Profiles> followingProfile = profilesRepository.findById(profileId);
        if (followingProfile.isEmpty()) {
            log.warn("존재하지 않는 유저 정보입니다.");
            return null;
        }

        if (myProfileId.equals(profileId)) {
            log.warn("본인 계정은 팔로우/언팔로우 할 수 없습니다.");
            return null;
        }

        Follows follows = Follows.builder()
            .followers(myProfile.get())
            .followings(followingProfile.get())
            .build();

        followsRepository.save(follows);

        return FollowsResponseDto.of(follows);
    }


    @Override
    @Transactional
    public void unfollow(Authentication auth, Long followingId) {

        Long myProfileId = Long.parseLong(auth.getName());

        Optional<Profiles> myProfile =
            profilesRepository.findById(myProfileId);
        if (myProfile.isEmpty()) {
            log.warn("존재하지 않는 유저 정보입니다.");
            return;
        }

        Optional<Profiles> followingProfile = profilesRepository.findById(followingId);
        if (followingProfile.isEmpty()) {
            log.warn("존재하지 않는 유저 정보입니다.");
            return;
        }

        if (myProfileId.equals(followingId)) {
            log.warn("본인 계정은 팔로우/언팔로우 할 수 없습니다.");
            return;
        }

        Optional<Follows> follows =
            followsRepository.findByFollowersAndFollowings(myProfile.get(), followingProfile.get());
        if (follows.isEmpty()) {
            log.warn("존재하지 않는 유저 정보입니다.");
            return;
        }

        followsRepository.delete(follows.get());
    }

    @Override
    public List<ProfilesResponseDto> findFollowings(Long profileId) {

        List<Follows> followings = followsRepository.findAllByFollowers_Id(profileId);
        List<Profiles> profiles = followings.stream()
            .map(Follows::getFollowings)
            .toList();

        return profiles.stream()
            .map(ProfilesResponseDto::of)
            .toList();
    }

    @Override
    public List<ProfilesResponseDto> findFollowers(Long profileId) {

        List<Follows> followers = followsRepository.findAllByFollowings_Id(profileId);
        List<Profiles> profiles = followers.stream()
            .map(Follows::getFollowers)
            .toList();

        return profiles.stream()
            .map(ProfilesResponseDto::of)
            .toList();
    }
}
