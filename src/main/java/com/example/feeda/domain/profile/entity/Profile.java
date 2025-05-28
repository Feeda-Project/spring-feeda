package com.example.feeda.domain.profile.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String nickname;

    private Date birth;

    private String bio;
}
