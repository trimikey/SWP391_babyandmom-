package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.CommentDTO;
import com.swp.BabyandMom.DTO.CreateCommentRequestDTO;
import com.swp.BabyandMom.Service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{blogId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBlog(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getCommentsByBlog(blogId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/blog/{blogId}")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long blogId,
            @RequestBody CreateCommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.createComment(blogId, requestDTO));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN') or @userUtils.getCurrentAccount().id == @commentService.getCommentOwnerId(#commentId)")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody CreateCommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.updateComment(commentId, requestDTO));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN') or @userUtils.getCurrentAccount().id == @commentService.getCommentOwnerId(#commentId)")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
