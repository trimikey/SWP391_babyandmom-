package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Entity.Transaction;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    Optional<Transaction> findByOrder(Order order);
} 