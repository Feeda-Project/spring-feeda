package com.example.feeda.domain.comment.service;

import com.example.feeda.domain.comment.dto.LikeCommentResponseDTO;
import com.example.feeda.domain.comment.entity.Comment;
import com.example.feeda.domain.comment.entity.CommentLike;
import com.example.feeda.domain.comment.repository.CommentLikeRepository;
import com.example.feeda.domain.comment.repository.CommentRepository;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ProfileRepository profileRepository;


    public LikeCommentResponseDTO likeComment(Long commentId, Long profileId) {
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByComment_IdAndProfile_Id(commentId, profileId);
        if(findCommentLike.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요한 댓글입니다. : " + commentId);
        }

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id 의 게시글이 존재하지 않습니다. : " + commentId)
        );

        Profile findProfile = profileRepository.findById(profileId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id 의 유저가 존재하지 않습니다. : " + profileId)
        );

        CommentLike commentLike = new CommentLike(findComment, findProfile);
        commentLikeRepository.save(commentLike);

        return new LikeCommentResponseDTO(commentLike);
    }

    public void unlikeComment(Long commentId, Long profileId) {
        Optional<CommentLike> findCommentLikeOptional = commentLikeRepository.findByComment_IdAndProfile_Id(commentId, profileId);
        if(findCommentLikeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아직 좋아요하지 않은 댓글 입니다. : " + commentId);
        }

        CommentLike commentLike = findCommentLikeOptional.get();

        commentLikeRepository.delete(commentLike);
    }
}
