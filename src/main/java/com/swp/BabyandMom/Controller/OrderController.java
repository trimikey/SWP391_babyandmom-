package com.swp.BabyandMom.Controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp.BabyandMom.DTO.OrderRequestDTO;
import com.swp.BabyandMom.DTO.OrderResponseDTO;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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




    //  API Tạo đơn hàng
    @PostMapping("/create")
    @Operation(summary = "Create Order by Membership Type")
    public ResponseEntity<OrderResponseDTO> createOrderByType(
            @Parameter(description = "Type of Membership Package", required = true)
            @RequestParam MembershipType membershipType) {
        return ResponseEntity.ok(orderService.createOrdersByType(membershipType));
    }

    @GetMapping("/payment-success/{id}")
    public ResponseEntity<String> successOrder(@PathVariable Long id){
        orderService.getPaymentSuccessURL(id);
        return ResponseEntity.ok("Order paid successfully");
    }


    // API Lấy danh sách đơn hàng theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }


    // API Thủ công cập nhật trạng thái đơn hàng hết hạn
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update-expired")
    public ResponseEntity<String> updateExpiredOrders() {
        orderService.updateExpiredOrders();
        return ResponseEntity.ok("Expired orders updated successfully");
    }

    // API Lấy danh sách tất cả đơn hàng
    @JsonIgnore
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getALlOrders();
        return ResponseEntity.ok(orders);
    }

    //  API Xem chi tiết đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        List<OrderResponseDTO> orders = orderService.getOrderById(id);
        return ResponseEntity.ok(orders);
    }

    //  API Hủy đơn hàng theo ID
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    //  API Xóa đơn hàng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    //  API Xóa hoàn toàn đơn hàng theo ID (chỉ dành cho ADMIN)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeOrder(@PathVariable Long id) {
        orderService.removeOrder(id);
        return ResponseEntity.ok("Order removed permanently from database");
    }

    @GetMapping("/payment-success/{id}")
    public ResponseEntity<String> successOrder(@PathVariable Long id){
        orderService.getPaymentSuccessURL(id);
        return ResponseEntity.ok("Order paid successfully");
    }
}

