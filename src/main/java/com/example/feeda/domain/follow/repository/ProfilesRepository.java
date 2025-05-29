package com.example.feeda.domain.follow.repository;

import com.example.feeda.domain.follow.entity.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends JpaRepository<Profiles, Long> {

}
