package com.example.feeda.domain.comment.controller;

import com.example.feeda.domain.comment.dto.CommentResponse;
import com.example.feeda.domain.comment.dto.CreateCommentRequest;
import com.example.feeda.domain.comment.dto.UpdateCommentRequest;
import com.example.feeda.domain.comment.service.CommentService;
import com.example.feeda.security.jwt.JwtPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 - 게시글에 대한 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @RequestBody CreateCommentRequest request
    ) {
        Long profileId = jwtPayload.getProfileId();
        CommentResponse response = commentService.createComment(postId, profileId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 수정 - 특정 댓글 수정
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

    // 댓글 삭제 - 특정 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        Long profileId = jwtPayload.getProfileId();
        commentService.deleteComment(commentId, profileId);
        return ResponseEntity.noContent().build();
    }
}


