package com.example.feeda.domain.post.service;

import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.post.dto.PostLikeResponseDTO;
import com.example.feeda.domain.post.dto.PostRequestDto;
import com.example.feeda.domain.post.dto.PostResponseDto;
import com.example.feeda.domain.post.entity.Post;
import com.example.feeda.domain.post.entity.PostLike;
import com.example.feeda.domain.post.repository.PostLikeRepository;
import com.example.feeda.domain.post.repository.PostRepository;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.security.jwt.JwtPayload;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository; // 현재 로그인 사용자 정보를 찾기 위해 필요
    private final FollowsRepository followsRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, JwtPayload jwtPayload) {

        Profile profile = profileRepository.findById(jwtPayload.getProfileId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다."));

        Post post = new Post(postRequestDto.getTitle(), postRequestDto.getContent(),
                postRequestDto.getCategory(), profile);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost, 0L);
    }

    @Override
    @Transactional
    public PostLikeResponseDTO makeLikes(Long id, JwtPayload jwtPayload) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));
        Profile profile = profileRepository.findById(jwtPayload.getProfileId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로필이 존재하지 않습니다."));
        ;

        // 중복 좋아요 방지
        postLikeRepository.findByPostAndProfile(post, profile).ifPresent(like -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다.");
        });

        PostLike savePost = postLikeRepository.save(new PostLike(post, profile));

        return new PostLikeResponseDTO(savePost);
    }

    @Override
    public void deleteLikes(Long id, Long profileId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필"));

        PostLike postLike = postLikeRepository.findByPostAndProfile(post, profile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자의 좋아요가 존재하지 않습니다."));

        postLikeRepository.delete(postLike);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto findPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다");
        }

        Post findPost = optionalPost.get();

        Long likeCount = postLikeRepository.countByPost(findPost);

        return new PostResponseDto(findPost, likeCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAll(Pageable pageable, String keyword) {

        return postRepository.findAllByTitleContaining(keyword, pageable)
                .map(post -> PostResponseDto.toDto(post, postLikeRepository.countByPost(post)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findFollowingAllPost(Pageable pageable, JwtPayload jwtPayload) {

        Page<Follows> followings = followsRepository.findAllByFollowers_Id(
                jwtPayload.getProfileId(), pageable);

        List<Long> followingProfileIds = followings.stream()
                .map(following -> following.getFollowings().getId())
                .toList();

        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.DESC, "updatedAt");

        return postRepository.findAllByProfile_IdIn(followingProfileIds, newPageable)
                .map(post -> PostResponseDto.toDto(post, postLikeRepository.countByPost(post)));
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, JwtPayload jwtPayload) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글"));

        if (!findPost.getProfile().getId().equals(jwtPayload.getProfileId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        findPost.update(requestDto.getTitle(), requestDto.getCategory(), requestDto.getCategory());
        Post savedPost = postRepository.save(findPost);

        Long likeCount = postLikeRepository.countByPost(findPost);

        return new PostResponseDto(savedPost, likeCount);
    }

    @Override
    @Transactional
    public void deletePost(Long id, JwtPayload jwtPayload) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글"));

        if (!findPost.getProfile().getId().equals(jwtPayload.getProfileId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        postRepository.delete(findPost);
    }
}
