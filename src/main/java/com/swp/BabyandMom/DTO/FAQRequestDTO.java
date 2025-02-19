package com.swp.BabyandMom.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FAQRequestDTO {
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
}