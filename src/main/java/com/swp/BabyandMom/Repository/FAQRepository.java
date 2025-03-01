package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByIsDeletedFalse();
    Optional<FAQ> findByIdAndIsDeletedFalse(Long id);
}