package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import com.swp.BabyandMom.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm các đơn hàng có trạng thái ACTIVE nhưng đã hết hạn
    List<Order> findByStatusAndEndDateBefore(OrderStatus status, LocalDateTime now);
    
    List<Order> findByStatus(OrderStatus status);
}
