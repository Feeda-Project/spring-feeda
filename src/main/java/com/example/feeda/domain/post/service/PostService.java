package com.example.feeda.domain.post.service;

import com.example.feeda.domain.post.dto.PostLikeResponseDTO;
import com.example.feeda.domain.post.dto.PostRequestDto;
import com.example.feeda.domain.post.dto.PostResponseDto;
import com.example.feeda.security.jwt.JwtPayload;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponseDto createPost(PostRequestDto postRequestDto,
        JwtPayload jwtPayload);

    PostResponseDto findPostById(Long id);

    Page<PostResponseDto> findAll(Pageable pageable, String keyword, LocalDateTime startUpdatedAt,
        LocalDateTime endUpdatedAt);

    Page<PostResponseDto> findFollowingAllPost(Pageable pageable, JwtPayload jwtPayload);

    PostResponseDto updatePost(Long id, PostRequestDto requestDto, JwtPayload jwtPayload);

    void deletePost(Long id, JwtPayload jwtPayload);

    PostLikeResponseDTO makeLikes(Long id, JwtPayload jwtPayload);

    void deleteLikes(Long id, Long profileId);

}