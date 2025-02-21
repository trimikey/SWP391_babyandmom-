package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.AlertStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Growth_Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Pregnancy_Profile pregnancy;

    @Column(nullable = false)
    private Integer pregnancyWeek;

    @Column(nullable = false)
    private Float PregnancyWeight;

    @Column(nullable = false)
    private Float PregnancyHeight;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private Float prePregnancyWeight;

    private Float prePregnancyHeight;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isNormal = true;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus = AlertStatus.NORMAL;

    @Column(columnDefinition = "TEXT")
    private String alertMessage;

    @OneToMany(mappedBy = "growthRecord")
    private List<Warning> warnings;
}