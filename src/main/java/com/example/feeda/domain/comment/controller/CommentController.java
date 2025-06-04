package com.example.feeda.domain.comment.controller;

import com.example.feeda.domain.comment.dto.CommentResponse;
import com.example.feeda.domain.comment.dto.CreateCommentRequest;
import com.example.feeda.domain.comment.dto.UpdateCommentRequest;
import com.example.feeda.domain.comment.service.CommentService;
import com.example.feeda.domain.comment.dto.LikeCommentResponseDTO;
import com.example.feeda.domain.comment.service.CommentLikeService;
import com.example.feeda.security.jwt.JwtPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comments/post/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        Long profileId = jwtPayload.getProfileId();
        CommentResponse response = commentService.createComment(postId, profileId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 전체 조회 (게시글 기준, 정렬/필터 가능)
    @GetMapping("/comments/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "latest") String sort // latest 또는 oldest
    ) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId, sort);
        return ResponseEntity.ok(comments);
    }

    // 댓글 단건 조회
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
        CommentResponse response = commentService.getCommentById(commentId);
        return ResponseEntity.ok(response);
    }


    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        Long profileId = jwtPayload.getProfileId();
        CommentResponse response = commentService.updateComment(commentId, profileId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        Long profileId = jwtPayload.getProfileId();
        commentService.deleteComment(commentId, profileId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 좋아요
    @PostMapping("/comments/{id}/likes")
    public ResponseEntity<LikeCommentResponseDTO> likeComment(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        Long profileId = jwtPayload.getProfileId();
        return new ResponseEntity<>(
                commentLikeService.likeComment(id, profileId),
                HttpStatus.OK
        );
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comments/{id}/likes")
    public ResponseEntity<Void> unlikeComment(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        commentLikeService.unlikeComment(id, jwtPayload.getProfileId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
