package com.swp.BabyandMom.DTO;

import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FAQResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}