package com.example.feeda.domain.comment.service;

import com.example.feeda.domain.comment.dto.CommentResponse;
import com.example.feeda.domain.comment.dto.CreateCommentRequest;
import com.example.feeda.domain.comment.dto.UpdateCommentRequest;
import com.example.feeda.domain.comment.entity.Comment;
import com.example.feeda.domain.comment.repository.CommentRepository;
import com.example.feeda.domain.post.entity.Post;
import com.example.feeda.domain.post.repository.PostRepository;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.exception.CustomResponseException;
import com.example.feeda.exception.enums.ResponseError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public CommentResponse createComment(Long postId, Long profileId, CreateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomResponseException(ResponseError.POST_NOT_FOUND));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomResponseException(ResponseError.PROFILE_NOT_FOUND));

        Comment comment = new Comment(post, profile, request.getContent());
        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    public List<CommentResponse> getCommentsByPostId(Long postId, String sort) {
        List<Comment> comments;

        if (sort.equalsIgnoreCase("oldest")) {
            comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        } else {
            comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        }

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomResponseException(ResponseError.COMMENT_NOT_FOUND));
        return CommentResponse.from(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long requesterProfileId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomResponseException(ResponseError.COMMENT_NOT_FOUND));

        if (!comment.getProfile().getId().equals(requesterProfileId)) {
            throw new CustomResponseException(ResponseError.NO_PERMISSION_TO_EDIT);
        }

        comment.updateContent(request.getContent());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long requesterProfileId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomResponseException(ResponseError.COMMENT_NOT_FOUND));

        Long authorId = comment.getProfile().getId();
        Long postOwnerId = comment.getPost().getProfile().getId();

        if (!authorId.equals(requesterProfileId) && !postOwnerId.equals(requesterProfileId)) {
            throw new CustomResponseException(ResponseError.NO_PERMISSION_TO_DELETE);
        }

        commentRepository.delete(comment);
    }
}
