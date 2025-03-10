package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import com.swp.BabyandMom.Entity.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(nullable = true)
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

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // Getters và Setters
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public void getPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private Membership_Package selectedPackage; // Gói mà user đã chọn
}