package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membership_Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MembershipType type;

    private String features;
    private BigDecimal price;
    private int durationInMonths;

    @OneToMany(mappedBy = "membershipPackage")
    private List<Subscription> subscriptions;
}