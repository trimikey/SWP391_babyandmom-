package com.swp.BabyandMom.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FAQRequestDTO {
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
}