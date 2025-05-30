package com.example.feeda.domain.post.repository;

import com.example.feeda.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByTitleContaining(String title, Pageable pageable);

}
