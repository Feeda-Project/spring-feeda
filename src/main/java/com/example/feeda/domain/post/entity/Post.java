package com.example.feeda.domain.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "longtext", nullable = false)
    private String content;

    @Column(length = 50)
    private String category;

    public void update(Long id, String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Post(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Post() {

    }

//    @ManyToOne
//    @JoinColumn(name = "profile_id")
//    private Profile profile;

}
