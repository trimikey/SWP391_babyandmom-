package com.swp.BabyandMom.DTO;

import java.time.LocalDateTime;
import com.swp.BabyandMom.Entity.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor




public class OrderResponseDTO {
    private Long id;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String packageType;
} 