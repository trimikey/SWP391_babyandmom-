package com.swp.BabyandMom.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swp.BabyandMom.Entity.Enum.ReminderType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReminderRequestDTO {
    private Long pregnancyId;

    private ReminderType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reminderDateTime;

    private String title;

    private String description;
}
