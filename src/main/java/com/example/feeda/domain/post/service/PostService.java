package com.example.feeda.domain.post.service;

import com.example.feeda.domain.post.dto.PostRequestDto;
import com.example.feeda.domain.post.dto.PostResponseDto;
import com.example.feeda.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {


    PostResponseDto createPost(String title, String content, String category);

    PostResponseDto findPostById(Long id);

    Page<PostResponseDto> findAll(Pageable pageable, String keyword);

    Post updatePost(Long id, PostRequestDto requestDto);

    void deletePost(Long id);
}
