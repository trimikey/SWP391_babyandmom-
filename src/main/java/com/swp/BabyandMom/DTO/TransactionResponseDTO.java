package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String userName;
    private Long orderId;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private String paymentMethod;
    private TransactionStatus status;
    private Boolean isDeleted;
}
