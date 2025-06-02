package com.example.feeda.domain.post.service;

import com.example.feeda.domain.follow.entity.Follows;
import com.example.feeda.domain.follow.repository.FollowsRepository;
import com.example.feeda.domain.post.dto.PostRequestDto;
import com.example.feeda.domain.post.dto.PostResponseDto;
import com.example.feeda.domain.post.entity.Post;
import com.example.feeda.domain.post.repository.PostRepository;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.exception.CustomResponseException;
import com.example.feeda.exception.enums.ResponseError;
import com.example.feeda.security.jwt.JwtPayload;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final FollowsRepository followsRepository;

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, JwtPayload jwtPayload) {

        Profile profile = profileRepository.findById(jwtPayload.getProfileId())
            .orElseThrow(
                () -> new CustomResponseException(ResponseError.PROFILE_NOT_FOUND));

        Post post = new Post(postRequestDto.getTitle(), postRequestDto.getContent(),
            postRequestDto.getCategory(), profile);
        
        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost.getId(),
            savedPost.getTitle(),
            savedPost.getContent(),
            savedPost.getCategory());
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto findPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            throw new CustomResponseException(ResponseError.POST_NOT_FOUND);
        }

        Post findPost = optionalPost.get();

        return new PostResponseDto(id, findPost.getTitle(), findPost.getContent(),
            findPost.getCategory());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAll(Pageable pageable, String keyword) {

        return postRepository.findAllByTitleContaining(keyword, pageable)
            .map(PostResponseDto::toDto);
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
            .map(PostResponseDto::toDto);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, JwtPayload jwtPayload) {

        Post findPost = postRepository.findById(id)
            .orElseThrow(() -> new CustomResponseException(ResponseError.POST_NOT_FOUND));

        if (!findPost.getProfile().getId().equals(jwtPayload.getProfileId())) {
            throw new CustomResponseException(ResponseError.NO_PERMISSION_TO_EDIT);
        }

        findPost.update(requestDto.getTitle(), requestDto.getCategory(), requestDto.getCategory());
        Post savedPost = postRepository.save(findPost);

        return new PostResponseDto(savedPost.getId(),
            savedPost.getTitle(),
            savedPost.getContent(),
            savedPost.getCategory());
    }

    @Override
    @Transactional
    public void deletePost(Long id, JwtPayload jwtPayload) {

        Post findPost = postRepository.findById(id)
            .orElseThrow(() -> new CustomResponseException(ResponseError.POST_NOT_FOUND));

        if (!findPost.getProfile().getId().equals(jwtPayload.getProfileId())) {
            throw new CustomResponseException(ResponseError.NO_PERMISSION_TO_DELETE);
        }

        postRepository.delete(findPost);
    }
}
