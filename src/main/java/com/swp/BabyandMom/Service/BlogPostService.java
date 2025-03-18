package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.BlogPostAdminResponseDTO;
import com.swp.BabyandMom.DTO.BlogPostRequestDTO;
import com.swp.BabyandMom.DTO.BlogPostResponseDTO;
import com.swp.BabyandMom.Entity.BlogPost;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.BlogPostRepository;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class BlogPostService {
    @Autowired
    private  BlogPostRepository repository;
    @Autowired
    private  UserUtils userUtils;

    public List<BlogPostResponseDTO> getAllPosts() {
        return repository.findByIsDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BlogPostAdminResponseDTO> getAllPostsForAdmin() {
        return repository.findAll().stream()
                .map(this::convertToAdminDTO)
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
        BlogPost post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));

        validateOwnershipOrAdmin(post.getUser());

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        repository.save(post);
        return convertToDTO(post);
    }

    public void deletePost(Long id) {
        BlogPost post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));

        validateOwnershipOrAdmin(post.getUser());

        post.setIsDeleted(true);
        post.setUpdatedAt(LocalDateTime.now());

        repository.save(post);
    }

    private void validateOwnershipOrAdmin(User postOwner) {
        User currentUser = userUtils.getCurrentAccount();
        if (currentUser == null ||
                (!postOwner.getId().equals(currentUser.getId()) &&
                        !currentUser.getRole().equals(RoleType.ADMIN))) {
            throw new AccessDeniedException("You do not have permission to modify this post");
        }
    }

    private BlogPostResponseDTO convertToDTO(BlogPost post) {
        return new BlogPostResponseDTO(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getUserName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
    private BlogPostAdminResponseDTO convertToAdminDTO(BlogPost post) {
        return new BlogPostAdminResponseDTO(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getContent(),
                post.getIsDeleted(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

}
