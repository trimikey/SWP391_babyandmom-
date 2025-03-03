package com.swp.BabyandMom.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Data
public class PregnancyProfileRequestDTO {

    @NotNull(message = "Baby name cannot be left blank")
    private String babyName;

    @NotNull(message = "Baby gender cannot be left blank")
    private String babyGender;

    @NotNull(message = "Due date cannot be left blank")
    private LocalDateTime dueDate;

    @NotNull(message = "Current week cannot be left blank")
    private Integer currentWeek;

    @NotNull(message = "Last Period cannot be left blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastPeriod;

    @NotNull(message = "Height cannot be left blank")
    private Float height;
}