package com.example.feeda.domain.comment.service;

import com.example.feeda.domain.comment.dto.LikeCommentResponseDTO;
import com.example.feeda.domain.comment.entity.Comment;
import com.example.feeda.domain.comment.entity.CommentLike;
import com.example.feeda.domain.comment.repository.CommentLikeRepository;
import com.example.feeda.domain.comment.repository.CommentRepository;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.exception.CustomResponseException;
import com.example.feeda.exception.enums.ResponseError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            throw new CustomResponseException(ResponseError.ALREADY_LIKED_COMMENT);
        }

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomResponseException(ResponseError.COMMENT_NOT_FOUND)
        );

        Profile findProfile = profileRepository.findById(profileId).orElseThrow(() ->
                new CustomResponseException(ResponseError.PROFILE_NOT_FOUND)
        );

        CommentLike commentLike = new CommentLike(findComment, findProfile);
        commentLikeRepository.save(commentLike);

        return new LikeCommentResponseDTO(commentLike);
    }

    public void unlikeComment(Long commentId, Long profileId) {
        Optional<CommentLike> findCommentLikeOptional = commentLikeRepository.findByComment_IdAndProfile_Id(commentId, profileId);
        if(findCommentLikeOptional.isEmpty()) {
            throw new CustomResponseException(ResponseError.NOT_YET_LIKED_COMMENT);
        }

        CommentLike commentLike = findCommentLikeOptional.get();

        commentLikeRepository.delete(commentLike);
    }
}
