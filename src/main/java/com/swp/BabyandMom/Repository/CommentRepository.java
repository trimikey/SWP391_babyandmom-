package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Comment;
import com.swp.BabyandMom.Entity.BlogPost;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogPost(BlogPost blogPost);
    List<Comment> findByUser(User user);
    List<Comment> findByBlogPostAndIsDeletedFalse(BlogPost blogPost);
}
