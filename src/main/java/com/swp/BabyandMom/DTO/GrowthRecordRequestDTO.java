package com.swp.BabyandMom.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrowthRecordRequestDTO {

    @NotNull(message = "Profile ID cannot be blank")
    private Long profileId;

    @NotNull(message = "Pregnancy week cannot be blank")
    private Integer pregnancyWeek;
    @NotNull(message = "Pregnancy weight cannot be blank")
    private Float pregnancyWeight;

    @NotNull(message = "Pregnancy height cannot be blank")
    private Float pregnancyHeight;

    private String notes;

    @NotNull(message = "Pre Pregnancy Weight cannot be blank")
    private Float prePregnancyWeight;
    @NotNull(message = "Pre Pregnancy Height cannot be blank")
    private Float prePregnancyHeight;
}
