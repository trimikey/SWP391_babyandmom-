package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.OrderRequestDTO;
import com.swp.BabyandMom.DTO.OrderResponseDTO;
import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1️⃣ API Tạo đơn hàng
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    // 2️⃣ API Lấy danh sách đơn hàng theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }


    // 3️⃣ API Thủ công cập nhật trạng thái đơn hàng hết hạn
    @PutMapping("/update-expired")
    public ResponseEntity<String> updateExpiredOrders() {
        orderService.updateExpiredOrders();
        return ResponseEntity.ok("Expired orders updated successfully");
    }

    // 4️⃣ API Lấy danh sách tất cả đơn hàng
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getALlOrders();
        return ResponseEntity.ok(orders);
    }

    // 5️⃣ API Xem chi tiết đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        List<OrderResponseDTO> orders = orderService.getOrderById(id);
        return ResponseEntity.ok(orders);
    }

    // 6️⃣ API Hủy đơn hàng theo ID
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}

