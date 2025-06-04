package com.example.feeda.domain.post.controller;

import com.example.feeda.domain.post.dto.PostLikeResponseDTO;
import com.example.feeda.domain.post.dto.PostRequestDto;
import com.example.feeda.domain.post.dto.PostResponseDto;
import com.example.feeda.domain.post.service.PostService;
import com.example.feeda.security.jwt.JwtPayload;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        PostResponseDto post = postService.createPost(requestDto, jwtPayload);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<PostLikeResponseDTO> makeLikes(
        @PathVariable Long id,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        return new ResponseEntity<>(postService.makeLikes(id, jwtPayload), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Void> deleteLikes(
        @PathVariable Long id,
        @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        Long profileId = jwtPayload.getProfileId();
        postService.deleteLikes(id, profileId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findPostById(@PathVariable @NotNull Long id) {
        return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> findAllPost(
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "10") @Min(1) int size,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startUpdatedAt,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endUpdatedAt
    ) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "updatedAt");

        return new ResponseEntity<>(
            postService.findAll(pageable, keyword, startUpdatedAt, endUpdatedAt), HttpStatus.OK);
    }

    @GetMapping("/followings")
    public ResponseEntity<Page<PostResponseDto>> findFollowingAllPost(
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "10") @Min(1) int size,
        @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return new ResponseEntity<>(postService.findFollowingAllPost(pageable, jwtPayload),
            HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable @NotNull Long id,
        @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal JwtPayload jwtPayload) {

        PostResponseDto post = postService.updatePost(id, requestDto, jwtPayload);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable @NotNull Long id,
        @AuthenticationPrincipal JwtPayload jwtPayload) {

        postService.deletePost(id, jwtPayload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
