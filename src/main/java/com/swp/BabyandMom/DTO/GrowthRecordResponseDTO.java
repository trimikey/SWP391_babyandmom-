package com.swp.BabyandMom.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GrowthRecordResponseDTO {
    private Long id;
    private Integer pregnancyWeek;
    private Float pregnancyWeight;
    private Float pregnancyHeight;
    private String notes;
    private Float prePregnancyWeight;
    private Float prePregnancyHeight;
    private LocalDateTime createdAt;
}
