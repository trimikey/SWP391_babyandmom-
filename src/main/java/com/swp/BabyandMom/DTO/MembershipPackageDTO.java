package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPackageDTO {
    private Long id;
    private MembershipType type;
    private String features;
    private BigDecimal price;
    private int durationInMonths;
} 