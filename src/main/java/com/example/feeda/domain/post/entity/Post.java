package com.example.feeda.domain.post.entity;

import com.example.feeda.domain.profile.entity.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "제목은 필수 항목입니다.")
    @Column(length = 100, nullable = false)
    private String title;

    @NotNull(message = "내용은 필수 항목입니다.")
    @Column(columnDefinition = "longtext", nullable = false)
    private String content;

    @Size(max = 50, message = "카테고리는 50자 이하로 입력하세요.")
    @Column(length = 50)
    private String category;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public void update(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Post(String title, String content, String category, Profile profile) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.profile = profile;
    }

    protected Post() {
    }
}
