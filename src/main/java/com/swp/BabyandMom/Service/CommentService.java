package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.CommentDTO;
import com.swp.BabyandMom.DTO.CreateCommentRequestDTO;
import com.swp.BabyandMom.Entity.BlogPost;
import com.swp.BabyandMom.Entity.Comment;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.BlogPostRepository;
import com.swp.BabyandMom.Repository.CommentRepository;
import com.swp.BabyandMom.Utils.UserUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserUtils userUtils;

    public CommentService(CommentRepository commentRepository, BlogPostRepository blogPostRepository, UserUtils userUtils) {
        this.commentRepository = commentRepository;
        this.blogPostRepository = blogPostRepository;
        this.userUtils = userUtils;
    }

    public List<CommentDTO> getCommentsByBlog(Long blogId) {
        BlogPost blogPost = blogPostRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        return commentRepository.findByBlogPostAndIsDeletedFalse(blogPost).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public CommentDTO createComment(Long blogId, CreateCommentRequestDTO requestDTO) {
        User currentUser = userUtils.getCurrentAccount();
        if (currentUser == null) {
            throw new RuntimeException("Unauthorized: User not logged in");
        }

        BlogPost blogPost = blogPostRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        Comment comment = new Comment();
        comment.setBlogPost(blogPost);
        comment.setUser(currentUser);
        comment.setContent(requestDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        return convertToDTO(comment);
    }

    public CommentDTO updateComment(Long commentId, CreateCommentRequestDTO requestDTO) {
        User currentUser = userUtils.getCurrentAccount();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!Objects.equals(comment.getUser().getId(), currentUser.getId()) &&
                !isAdmin(currentUser)) {
            throw new AccessDeniedException("Unauthorized");
        }

        comment.setContent(requestDTO.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        return convertToDTO(comment);
    }

    public void deleteComment(Long commentId) {
        User currentUser = userUtils.getCurrentAccount();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!Objects.equals(comment.getUser().getId(), currentUser.getId()) &&
                !isAdmin(currentUser)) {
            throw new AccessDeniedException("Unauthorized");
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }


    public Long getCommentOwnerId(Long commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.getUser().getId())
                .orElse(null);
    }

    private boolean isAdmin(User user) {
        return user.getRole() instanceof RoleType
                ? user.getRole() == RoleType.ADMIN
                : "ADMIN".equalsIgnoreCase(user.getRole().toString());
    }

    private CommentDTO convertToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getBlogPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getFullName(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
