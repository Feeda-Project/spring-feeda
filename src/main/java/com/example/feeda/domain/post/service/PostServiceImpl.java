package com.example.feeda.domain.post.service;

import com.example.feeda.domain.post.dto.PostRequestDto;
import com.example.feeda.domain.post.dto.PostResponseDto;
import com.example.feeda.domain.post.entity.Post;
import com.example.feeda.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostResponseDto createPost(String title, String content, String category) {
        Post post = new Post(title, content, category);
        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCategory());
    }

    @Override
    public PostResponseDto findPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다");
        }

        Post findPost = optionalPost.get();

        return new PostResponseDto(id, findPost.getTitle(), findPost.getContent(), findPost.getCategory());
    }

    @Override
    public Page<PostResponseDto> findAll(Pageable pageable, String keyword) {
        return postRepository.findAllByTitleContaining(keyword, pageable).map(PostResponseDto::toDto);
    }

    @Override
    public Post updatePost(Long id, PostRequestDto requestDto) {
        Post findPost = postRepository.findPostByIdOrElseThrow(id);

        if (findPost == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글");
        }

        findPost.update(id, requestDto.getTitle(), requestDto.getCategory(), requestDto.getCategory());
        postRepository.save(findPost);

        return findPost;
    }

    @Override
    public void deletePost(Long id) {
        Post findPost = postRepository.findPostByIdOrElseThrow(id);

        postRepository.delete(findPost);
    }
}
