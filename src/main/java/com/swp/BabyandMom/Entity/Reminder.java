package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.ReminderType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private LocalDateTime reminderDateTime;

    private String title;
    private String description;
}