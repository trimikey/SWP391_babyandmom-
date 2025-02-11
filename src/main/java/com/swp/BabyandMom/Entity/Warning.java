package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.SeverityLevel;
import com.swp.BabyandMom.Entity.Enum.WarningType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Pregnancy_Profile pregnancy;

    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private Growth_Record growthRecord;

    @ManyToOne
    @JoinColumn(name = "standard_id", nullable = false)
    private GrowthStandard growthStandard;

    @Enumerated(EnumType.STRING)
    private WarningType warnType;

    @Enumerated(EnumType.STRING)
    private SeverityLevel severityLevel;

    @Column(nullable = false)
    private String message;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}