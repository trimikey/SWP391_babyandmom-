package com.swp.BabyandMom.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data


public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public BlogPost(Long id, User user, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted, List<Comment> comments) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.comments = comments;
    }

    public BlogPost( ) {

    }
}