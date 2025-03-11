package com.swp.BabyandMom.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.swp.BabyandMom.Entity.Enum.ReminderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Pregnancy_Profile pregnancy;

    @Enumerated(EnumType.STRING)
    private ReminderType type;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reminderDateTime;

    private String title;
    private String description;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}