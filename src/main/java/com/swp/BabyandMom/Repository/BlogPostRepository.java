package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.BlogPost;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByUser(User user);
    List<BlogPost> findByIsDeletedFalse();
    Optional<BlogPost> findByIdAndIsDeletedFalse(Long id);
}
