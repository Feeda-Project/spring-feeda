package com.example.feeda.domain.follow.repository;

import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.profile.entity.Profile;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {

    Optional<Follows> findByFollowersAndFollowings(Profile followers, Profile followings);

    Page<Follows> findAllByFollowings_Id(Long followingsId, Pageable pageable);

    Page<Follows> findAllByFollowers_Id(Long followersId, Pageable pageable);
}
