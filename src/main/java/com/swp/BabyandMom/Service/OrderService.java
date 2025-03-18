package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.OrderRequestDTO;
import com.swp.BabyandMom.DTO.OrderResponseDTO;


import com.swp.BabyandMom.DTO.OrderResponseDTO2;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import com.swp.BabyandMom.Entity.Enum.PaymentStatus;
import com.swp.BabyandMom.Entity.Membership_Package;
import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.MembershipPackageRepository;
import com.swp.BabyandMom.Repository.OrderRepository;
import com.swp.BabyandMom.Repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.swp.BabyandMom.Entity.Subscription;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service


public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private MembershipPackageRepository membershipPackageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    public void updatePaymentStatus(Long orderId, PaymentStatus status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setPaymentStatus(status);

            if (status == PaymentStatus.COMPLETED) {
                order.setStatus(OrderStatus.PAID);
            }
            else if (status == PaymentStatus.FAILED) {
                order.setStatus(OrderStatus.CANCELED);
            }

            orderRepository.save(order);
        });
    }



    public OrderResponseDTO createOrder(OrderRequestDTO orderDTO) {
        User user = userService.getAccountByEmail(orderDTO.getBuyerEmail());
        Membership_Package  selectedPackage = membershipPackageRepository.findByType(orderDTO.getType())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setMembershipPackage(selectedPackage);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(selectedPackage.getDurationInMonths()));
        subscription.setIsActive(true);
        subscription = subscriptionRepository.save(subscription);

        Order order = new Order();
        order.setUser(user);
        order.setSubscription(subscription);
        order.setBuyerName(user.getName());
        order.setBuyerEmail(user.getEmail());
        order.setBuyerPhone(user.getPhoneNumber());
        order.setTotalPrice(selectedPackage.getPrice().doubleValue());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setStartDate(subscription.getStartDate());
        order.setEndDate(subscription.getEndDate());
        order.setIsDeleted(false);

        Order savedOrder = orderRepository.save(order);

        
        return new OrderResponseDTO(
            savedOrder.getId(),
            savedOrder.getBuyerName(),
            savedOrder.getBuyerEmail(),
            savedOrder.getBuyerPhone(),
            savedOrder.getTotalPrice(),
            savedOrder.getStatus(),
            savedOrder.getCreatedAt(),
            savedOrder.getStartDate(),
            savedOrder.getEndDate(),
            savedOrder.getSubscription().getMembershipPackage().getType().toString()
        );
    }

    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getBuyerName(),
                        order.getBuyerEmail(),
                        order.getBuyerPhone(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getStartDate(),
                        order.getEndDate(),
                        order.getCreatedAt(),

                        order.getSubscription().getMembershipPackage().getType().toString()
                ))
                .collect(Collectors.toList());
    }

    public String getPaymentSuccessURL(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PAID);
            order.setPaymentStatus(PaymentStatus.COMPLETED);
            orderRepository.save(order);
        } else if (order.getStatus() == OrderStatus.PAID && order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return "https://BabyAndMom.com/payment-success?orderId=" + orderId;
        } else {
            throw new RuntimeException("Payment not completed for this order");
        }

            return "https://BabyAndMom.com/payment-success?orderId=" + orderId;
    }



    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(" Order not found"));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELED);
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);
        } else {
            throw new RuntimeException(" Cannot cancel an active or expired order");
        }
    }


    public OrderResponseDTO createOrdersByType(MembershipType membershipType) {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getAccountByEmail(loggedInEmail);
        Membership_Package selectedPackage = membershipPackageRepository.findByType(membershipType)
                .orElseThrow(() -> new RuntimeException("Type not found"));
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setMembershipPackage(selectedPackage);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(selectedPackage.getDurationInMonths()));
        subscription.setIsActive(true);
        subscription = subscriptionRepository.save(subscription);

        Order order = new Order();
        order.setUser(user);
        order.setSubscription(subscription);
        order.setBuyerName(user.getName());
        order.setBuyerEmail(user.getEmail());
        order.setBuyerPhone(user.getPhoneNumber());
        order.setTotalPrice(selectedPackage.getPrice().doubleValue());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setStartDate(subscription.getStartDate());
        order.setEndDate(subscription.getEndDate());
        order.setIsDeleted(false);

        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDTO(
                savedOrder.getId(),
                savedOrder.getBuyerName(),
                savedOrder.getBuyerEmail(),
                savedOrder.getBuyerPhone(),
                savedOrder.getTotalPrice(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt(),
                savedOrder.getStartDate(),
                savedOrder.getEndDate(),
                savedOrder.getSubscription().getMembershipPackage().getType().toString()
        );
    }


    //    Nếu người dùng chưa thanh toán (PENDING), họ có thể hủy Order.
    //    Nếu Order đã ACTIVE, không thể hủy.

    // Cập nhật trạng thái đơn hàng khi thanh toán thành công

    public void confirmPayment(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getStatus() == OrderStatus.PENDING){
            order.setStatus(OrderStatus.ACTIVE);
            orderRepository.save(order);
        }else {
            throw new RuntimeException("Order is not in PENDING state");
        }
    }

    // Kiểm tra hạn sử dụng của gói

    public boolean isSupcriptionIsActive(Long oderId){
        Order order = orderRepository.findById(oderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getEndDate().isAfter(LocalDateTime.now()) && order.getStatus() == OrderStatus.ACTIVE;
    }


    // Cập nhật thử công trạng thái của đơn hàng
    public void updateOrderStatus(Long orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    // Tự động set trạng thái hết hạn cho gói nếu quá hạn ngày sử dụng
    @Scheduled(cron = "0 0 0 * * ?")    // anotation set chạy tự động
    public void updateExpiredOrders() {
        // Lây ra danh sách đơn hàng ở trạng thái ACTIVE nhưng quá quá date

        List<Order> expiredOrders = orderRepository.findByStatusAndEndDateBefore(OrderStatus.ACTIVE, LocalDateTime.now());

        for(Order order : expiredOrders){
            order.setStatus(OrderStatus.EXPIRED);
        }

        orderRepository.saveAll(expiredOrders);

        System.out.println("Updated order status successful");
    }



    public List<Order> getALlOrders(){
        return orderRepository.findAll();
    }


    public List<OrderResponseDTO2> getOrderById(Long id) {
        return orderRepository.findById(id).stream()
                .map(order -> new OrderResponseDTO2(
                        order.getId(),
                        order.getBuyerName(),
                        order.getBuyerEmail(),
                        order.getBuyerPhone(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getCreatedAt(),
                        order.getStartDate(),
                        order.getEndDate(),
                        order.getSubscription().getMembershipPackage().getType().toString(),
                        order.getUser().getId()
                ))
                .collect(Collectors.toList());
    }



//    public void deleteOrder(Long orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//
//        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CANCELED) {
//            order.setIsDeleted(true);
//            orderRepository.save(order);
//        } else {
//            throw new RuntimeException("Cannot delete an active or expired order");
//        }
//    }
public void deleteOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.PAID) {
        order.setIsDeleted(true);
        orderRepository.save(order);
    } else {
        throw new RuntimeException("Cannot delete an active or expired order");
    }
}

    // Xóa hoàn toàn đơn hàng khỏi database (hard delete)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Chỉ admin mới có thể xóa hoàn toàn và chỉ xóa được đơn hàng đã bị soft delete
        if (order.getIsDeleted()) {
            // Xóa subscription liên quan
            if (order.getSubscription() != null) {
                subscriptionRepository.delete(order.getSubscription());
            }
            // Xóa order
            orderRepository.delete(order);
        } else {
            throw new RuntimeException("Cannot remove order that has not been soft deleted first");
        }
    }

}


