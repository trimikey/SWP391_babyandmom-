package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(nullable = false)
    private String buyerName;

    @Column(nullable = false)
    private String buyerPhone;

    @Column(nullable = false)
    private String buyerEmail;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToOne(mappedBy = "order")
    private Transaction transaction;

    @Column(nullable = false)
    private LocalDateTime startDate; // Ngày kích hoạt gói

    @Column(nullable = false)
    private LocalDateTime endDate; // Ngày hết hạn gói

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private Package selectedPackage; // Gói mà user đã chọn
}