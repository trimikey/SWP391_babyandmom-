package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.BlogPostRequestDTO;
import com.swp.BabyandMom.DTO.BlogPostResponseDTO;
import com.swp.BabyandMom.Entity.BlogPost;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.BlogPostRepository;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostService {
    private final BlogPostRepository repository;
    private final UserUtils userUtils;

    public List<BlogPostResponseDTO> getAllPosts() {
        return repository.findByIsDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BlogPostResponseDTO getPostById(Long id) {
        BlogPost post = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        return convertToDTO(post);
    }

    public BlogPostResponseDTO createPost(BlogPostRequestDTO request) {
        User currentUser = userUtils.getCurrentAccount();
        if (currentUser == null) {
            throw new RuntimeException("User must be logged in to create a post");
        }

        BlogPost post = new BlogPost();
        post.setUser(currentUser);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setIsDeleted(false);

        repository.save(post);
        return convertToDTO(post);
    }

    public BlogPostResponseDTO updatePost(Long id, BlogPostRequestDTO request) {
        BlogPost post = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));

        validateOwnership(post.getUser());

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        repository.save(post);
        return convertToDTO(post);
    }

    public void deletePost(Long id) {
        BlogPost post = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));

        validateOwnership(post.getUser());

        post.setIsDeleted(true);
        post.setUpdatedAt(LocalDateTime.now());

        repository.save(post);
    }

    private void validateOwnership(User postOwner) {
        User currentUser = userUtils.getCurrentAccount();
        if (currentUser == null || !postOwner.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this post");
        }
    }

    private BlogPostResponseDTO convertToDTO(BlogPost post) {
        return new BlogPostResponseDTO(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
