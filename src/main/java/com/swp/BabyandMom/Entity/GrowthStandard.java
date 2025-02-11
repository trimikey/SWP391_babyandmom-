package com.swp.BabyandMom.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrowthStandard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer pregnancyWeek;

    @Column(nullable = false)
    private Float minWeight;

    @Column(nullable = false)
    private Float maxWeight;

    @Column(nullable = false)
    private Float minLength;

    @Column(nullable = false)
    private Float maxLength;

    @Column(nullable = false)
    private String gender;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "growthStandard")
    private List<Warning> warnings;
}