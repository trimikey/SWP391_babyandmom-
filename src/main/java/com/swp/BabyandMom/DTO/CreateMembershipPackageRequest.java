package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.MembershipType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateMembershipPackageRequest {
    private MembershipType type;
    private String features;
    private BigDecimal price;
    private int durationInMonths;
} 