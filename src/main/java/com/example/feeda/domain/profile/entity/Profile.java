package com.example.feeda.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private Date birth;

    private String bio;


    public static Profile of(String nickname, Date birth, String bio) {
        return new Profile(null, nickname, birth, bio, null, null);
    }

    public Profile(Long id, String nickname, Date birth, String bio, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(updatedAt, createdAt);
        this.id = id;
        this.nickname = nickname;
        this.birth = birth;
        this.bio = bio;

    }

    public void updateProfile(String nickname, Date birth, String bio) {
        this.nickname = nickname;
        this.birth = birth;
        this.bio = bio;
    }
}
