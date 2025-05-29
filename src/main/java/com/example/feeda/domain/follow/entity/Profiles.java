package com.example.feeda.domain.follow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * 프로필 Entity. 추후 프로필 개발 병합 이후 삭제 예정.
 */

@Entity
@Getter
@Table(name = "profiles")
public class Profiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts accounts;

    @JsonIgnore
    @OneToMany(mappedBy = "followers", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follows> followers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "followings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follows> followings = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
