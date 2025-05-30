package com.example.feeda.domain.comment.controller;

import com.example.feeda.domain.comment.dto.LikeCommentResponseDTO;
import com.example.feeda.domain.comment.service.CommentLikeService;
import com.example.feeda.security.jwt.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/comments/{id}/likes")
    public ResponseEntity<LikeCommentResponseDTO> likeComment(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        return new ResponseEntity<>(commentLikeService.likeComment(id, jwtPayload.getProfileId()), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}/likes")
    public ResponseEntity<Void> unlikeComment(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtPayload jwtPayload
    ) {
        commentLikeService.unlikeComment(id, jwtPayload.getProfileId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
