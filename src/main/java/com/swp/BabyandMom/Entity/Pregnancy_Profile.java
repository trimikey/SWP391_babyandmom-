package com.swp.BabyandMom.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pregnancy_Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String babyName;

    @Column(nullable = false)
    private String babyGender;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private Integer currentWeek;

    @Column(nullable = false)
    private String lastPeriod;

    @Column(nullable = false)
    private Float height;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isNormal = true;

    @OneToMany(mappedBy = "pregnancy", cascade = CascadeType.ALL)
    private List<Growth_Record> growthRecords;

    @OneToMany(mappedBy = "pregnancy", cascade = CascadeType.ALL)
    private List<Reminder> reminders;

    @OneToMany(mappedBy = "pregnancy", cascade = CascadeType.ALL)
    private List<Warning> warnings;
}