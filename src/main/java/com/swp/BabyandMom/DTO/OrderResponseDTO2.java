package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO2 {
    private Long id;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String packageType;
    private Long userId;
}