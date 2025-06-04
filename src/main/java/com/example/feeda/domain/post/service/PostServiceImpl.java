package com.example.feeda.domain.post.service;

import com.example.feeda.domain.comment.repository.CommentRepository;
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
import com.example.feeda.exception.CustomResponseException;
import com.example.feeda.exception.enums.ResponseError;
import com.example.feeda.security.jwt.JwtPayload;
import java.time.LocalDate;
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
    private final ProfileRepository profileRepository; // 현재 로그인 사용자 정보를 찾기 위해 필요
    private final FollowsRepository followsRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, JwtPayload jwtPayload) {

        Profile profile = profileRepository.findById(jwtPayload.getProfileId())
            .orElseThrow(
                () -> new CustomResponseException(ResponseError.PROFILE_NOT_FOUND));

        Post post = new Post(postRequestDto.getTitle(), postRequestDto.getContent(),
            postRequestDto.getCategory(), profile);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost, 0L, 0L);
    }

    @Override
    @Transactional
    public PostLikeResponseDTO makeLikes(Long id, JwtPayload jwtPayload) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomResponseException(ResponseError.POST_NOT_FOUND));
        Profile profile = profileRepository.findById(jwtPayload.getProfileId()).orElseThrow(() -> new CustomResponseException(ResponseError.PROFILE_NOT_FOUND));

        // 중복 좋아요 방지
        postLikeRepository.findByPostAndProfile(post, profile).ifPresent(like -> {
            throw new CustomResponseException(ResponseError.ALREADY_LIKED_POST);
        });

        PostLike savePost = postLikeRepository.save(new PostLike(post, profile));

        return new PostLikeResponseDTO(savePost);
    }

    @Override
    public void deleteLikes(Long id, Long profileId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomResponseException(ResponseError.POST_NOT_FOUND));

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomResponseException(ResponseError.PROFILE_NOT_FOUND));

        PostLike postLike = postLikeRepository.findByPostAndProfile(post, profile)
                .orElseThrow(() -> new CustomResponseException(ResponseError.NOT_YET_LIKED_POST));

        postLikeRepository.delete(postLike);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto findPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            throw new CustomResponseException(ResponseError.POST_NOT_FOUND);
        }

        Post findPost = optionalPost.get();

        Long likeCount = postLikeRepository.countByPost(findPost);
        Long commentCount = commentRepository.countByPost(findPost);

        return new PostResponseDto(findPost, likeCount, commentCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAll(Pageable pageable, String keyword,
        LocalDate startUpdatedAt, LocalDate endUpdatedAt) {

        if ((startUpdatedAt == null && endUpdatedAt != null) || (startUpdatedAt != null
            && endUpdatedAt == null)) {
            throw new CustomResponseException(ResponseError.INVALID_DATE_PARAMETERS);
        }

        if (startUpdatedAt != null) {
            return postRepository.findAllByTitleContainingAndUpdatedAtBetween(
                    keyword, startUpdatedAt.atStartOfDay(), endUpdatedAt.atTime(23, 59, 59), pageable)
                .map(post -> new PostResponseDto(
                        post,
                        postLikeRepository.countByPost(post),
                        commentRepository.countByPost(post)
                ));
        }

        return postRepository.findAllByTitleContaining(keyword, pageable)
            .map(post -> new PostResponseDto(
                    post,
                    postLikeRepository.countByPost(post),
                    commentRepository.countByPost(post)
            ));
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
            .map(post -> new PostResponseDto(
                    post,
                    postLikeRepository.countByPost(post),
                    commentRepository.countByPost(post)
            ));
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, JwtPayload jwtPayload) {

        Post findPost = postRepository.findById(id)
            .orElseThrow(() -> new CustomResponseException(ResponseError.POST_NOT_FOUND));

        if (!findPost.getProfile().getId().equals(jwtPayload.getProfileId())) {
            throw new CustomResponseException(ResponseError.NO_PERMISSION_TO_EDIT);
        }

        findPost.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory());
        Post savedPost = postRepository.save(findPost);

        Long likeCount = postLikeRepository.countByPost(findPost);
        Long commentCount = commentRepository.countByPost(findPost);

        return new PostResponseDto(savedPost, likeCount, commentCount);
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
