package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.BlogPostRequestDTO;
import com.swp.BabyandMom.DTO.BlogPostResponseDTO;
import com.swp.BabyandMom.Service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogPostController {
    private final BlogPostService service;

    @GetMapping
    public ResponseEntity<List<BlogPostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(service.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponseDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostById(id));
    }
    @PostMapping
    public ResponseEntity<BlogPostResponseDTO> createPost(@RequestBody BlogPostRequestDTO request) {
        return ResponseEntity.ok(service.createPost(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostResponseDTO> updatePost(@PathVariable Long id, @RequestBody BlogPostRequestDTO request) {
        return ResponseEntity.ok(service.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.ok("Blog post deleted successfully");
    }
}
