package com.example.feeda.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.feeda.domain.account.entity.Account;
import com.example.feeda.domain.follow.dto.FollowsResponseDto;
import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.follow.service.FollowsServiceImpl;
import com.example.feeda.domain.profile.dto.ProfileListResponseDto;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.security.jwt.JwtPayload;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @InjectMocks
    FollowsServiceImpl followsServiceimpl;

    @Mock
    FollowsRepository followsRepository;

    @Mock
    ProfileRepository profileRepository;

    @Test
    void 계정_팔로우_단위_테스트() {

        //given
        Long myAccountId = 1L;
        Long myProfileId = 1L;
        Long targetProfileId = 2L;
        LocalDateTime now = LocalDateTime.now();

        JwtPayload jwtPayload = new JwtPayload(myAccountId, myProfileId, "test@naver.com", "test");

        Account myAccount = new Account();
        Profile myProfile = new Profile(myProfileId, "test", new Date(), "intro myself", now, now,
            myAccount);

        Account targetAccount = new Account();
        Profile targetProfile = new Profile(targetProfileId, "target", new Date(), "intro target",
            now, now, targetAccount);

        when(profileRepository.findById(myProfileId)).thenReturn(Optional.of(myProfile));
        when(profileRepository.findById(targetProfileId)).thenReturn(Optional.of(targetProfile));
        when(followsRepository.findByFollowersAndFollowings(myProfile, targetProfile))
            .thenReturn(Optional.empty());

        Follows newFollow = Follows.builder()
            .followers(myProfile)
            .followings(targetProfile)
            .build();

        when(followsRepository.save(any(Follows.class))).thenReturn(newFollow);

        //when
        FollowsResponseDto followsResponseDto = followsServiceimpl.follow(jwtPayload,
            targetProfileId);

        //then
        assertThat(followsResponseDto.getFollowerId()).isEqualTo(myProfileId);
        assertThat(followsResponseDto.getFollowingId()).isEqualTo(targetProfileId);
    }

    @Test
    void 계정_언팔로우_단위_테스트() {

        //given
        Long myAccountId = 1L;
        Long myProfileId = 1L;
        Long followingProfileId = 2L;
        LocalDateTime now = LocalDateTime.now();

        JwtPayload jwtPayload = new JwtPayload(myAccountId, myProfileId, "test@naver.com", "test");

        Account myAccount = new Account();
        Profile myProfile = new Profile(myProfileId, "test", new Date(), "intro myself", now, now,
            myAccount);

        Account targetAccount = new Account();
        Profile targetProfile = new Profile(followingProfileId, "target", new Date(),
            "intro target",
            now, now, targetAccount);

        Follows follow = Follows.builder()
            .followers(myProfile)
            .followings(targetProfile)
            .build();

        when(profileRepository.findById(myProfileId)).thenReturn(Optional.of(myProfile));
        when(profileRepository.findById(followingProfileId)).thenReturn(Optional.of(targetProfile));
        when(followsRepository.findByFollowersAndFollowings(myProfile, targetProfile))
            .thenReturn(Optional.of(follow));
        doNothing().when(followsRepository).delete(follow);

        //when
        followsServiceimpl.unfollow(jwtPayload,
            followingProfileId);

        // then
        verify(followsRepository, times(1)).delete(follow);
    }

    @Test
    void 팔로워_목록_조회_단위_테스트() {
        Long myAccountId = 1L;
        Long firstProfileId = 1L;
        Long secondProfileId = 2L;
        Long followingProfileId = 3L;
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);

        JwtPayload jwtPayload = new JwtPayload(myAccountId, firstProfileId, "test@naver.com",
            "firstNickname");

        Account myAccount = new Account();
        Profile firstProfile = new Profile(firstProfileId, "firstNickname", new Date(),
            "intro myself", now,
            now,
            myAccount);

        Account secondAccount = new Account();
        Profile secondProfile = new Profile(secondProfileId, "secondNickname", new Date(),
            "intro myself", now,
            now,
            secondAccount);

        Account targetAccount = new Account();
        Profile targetProfile = new Profile(followingProfileId, "targetNickname", new Date(),
            "intro target",
            now, now, targetAccount);

        Follows firstFollow = Follows.builder().followers(firstProfile).followings(targetProfile)
            .build();
        Follows secondFollow = Follows.builder().followers(secondProfile).followings(targetProfile)
            .build();

        Page<Follows> followsPage = new PageImpl<>(List.of(firstFollow, secondFollow), pageable, 2);
        when(followsRepository.findAllByFollowings_Id(followingProfileId, pageable)).thenReturn(
            followsPage);

        // when
        ProfileListResponseDto result = followsServiceimpl.findFollowersPage(followingProfileId,
            jwtPayload, pageable);

        // then
        assertThat(result.getProfiles().get(0).getNickname()).isEqualTo("firstNickname");
        assertThat(result.getProfiles().get(1).getNickname()).isEqualTo("secondNickname");
        assertThat(result.getCurrentPage()).isEqualTo(1);
    }

    @Test
    void 팔로잉_목록_조회_단위_테스트() {
        Long myAccountId = 1L;
        Long firstProfileId = 1L;
        Long secondProfileId = 2L;
        Long followingProfileId = 3L;
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);

        JwtPayload jwtPayload = new JwtPayload(myAccountId, firstProfileId, "test@naver.com",
            "firstNickname");

        Account myAccount = new Account();
        Profile firstProfile = new Profile(firstProfileId, "firstNickname", new Date(),
            "intro myself", now,
            now,
            myAccount);

        Account secondAccount = new Account();
        Profile secondProfile = new Profile(secondProfileId, "secondNickname", new Date(),
            "intro myself", now,
            now,
            secondAccount);

        Account targetAccount = new Account();
        Profile targetProfile = new Profile(followingProfileId, "targetNickname", new Date(),
            "intro target",
            now, now, targetAccount);

        Follows firstFollow = Follows.builder().followers(firstProfile).followings(secondProfile)
            .build();
        Follows secondFollow = Follows.builder().followers(firstProfile).followings(targetProfile)
            .build();

        Page<Follows> followsPage = new PageImpl<>(List.of(firstFollow, secondFollow), pageable, 2);
        when(followsRepository.findAllByFollowers_Id(firstProfileId, pageable)).thenReturn(
            followsPage);

        // when
        ProfileListResponseDto result = followsServiceimpl.findFollowingsPage(firstProfileId,
            jwtPayload, pageable);

        // then
        assertThat(result.getProfiles().get(0).getNickname()).isEqualTo("secondNickname");
        assertThat(result.getProfiles().get(1).getNickname()).isEqualTo("targetNickname");
        assertThat(result.getCurrentPage()).isEqualTo(1);
    }
}
