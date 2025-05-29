package com.example.feeda.domain.profile.entity;

import com.example.feeda.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private Date birth;

    private String bio;

    @Setter
    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    //프로필 생성해줄때 사용
    public static Profile create(String nickname, Date birth, String bio) {
        return new Profile(null, nickname, birth, bio, null, null);
    }

    public Profile(String nickname, Date birth, String bio) {
        this.nickname = nickname;
        this.birth = birth;
        this.bio = bio;
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
