package com.example.feeda.domain.profile.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "profile")
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String nickname;

    private Date birth;

    private String bio;

    /**
     * JPA에서 기본으로 사용되는 기본 생성자
     */
    public Profile () {}

    /**
     *
     * getter
     */

    public Long getAccountId() {
        return accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public Date getBirth() {
        return birth;
    }

    public String getBio() {
        return bio;
    }
}
