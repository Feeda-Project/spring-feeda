package com.example.feeda.domain.profile.repository;

import com.example.feeda.domain.profile.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    /**
     * 닉네임으로 목록 검색(페이징)
     */

    Page<Profile> findByNicknameContaining(String nickname, Pageable pageable);

}
