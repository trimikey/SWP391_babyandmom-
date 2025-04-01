package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndEndDateBefore(OrderStatus status, LocalDateTime now);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByUserAndIsDeletedFalse(User user);

    void delete(Order order);

    Optional<Order> findBySubscriptionId(Long subscriptionId);

}
