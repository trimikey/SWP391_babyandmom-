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
    private Float pregnancyWeight;

    @Column(nullable = true)
    private Float pregnancyHeight;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Float prePregnancyWeight;

    @Column(nullable = true)
    private Float prePregnancyHeight;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    public float getPrePregnancyBMI() {
        return prePregnancyWeight / ((prePregnancyHeight / 100) * (prePregnancyHeight / 100));
    }

    public float getCurrentBMI() {
        return pregnancyWeight / ((pregnancyHeight / 100) * (pregnancyHeight / 100));
    }

}
