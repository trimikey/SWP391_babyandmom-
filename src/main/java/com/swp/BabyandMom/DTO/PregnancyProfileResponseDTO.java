package com.swp.BabyandMom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PregnancyProfileResponseDTO {
    private Long id;
    private String babyName;
    private String babyGender;
    private LocalDateTime dueDate;
    private Integer currentWeek;
    private LocalDateTime lastPeriod;
    private Float height;
}