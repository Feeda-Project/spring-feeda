package com.example.feeda.domain.follow.repository;

import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.profile.entity.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {

    Optional<Follows> findByFollowersAndFollowings(Profile followers, Profile followings);

    List<Follows> findAllByFollowers_Id(Long followerId);

    List<Follows> findAllByFollowings_Id(Long followingId);
}
