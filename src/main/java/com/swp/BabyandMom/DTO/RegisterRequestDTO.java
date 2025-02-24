package com.swp.BabyandMom.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RegisterRequestDTO {
    // Thông tin user hiện tại
    private String name;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;

    // Thêm thông tin thai kỳ
    private LocalDateTime dueDate;
    private Integer currentWeek;
    private String lastPeriod;
    private Float height;

    public RegisterRequestDTO(String name, String userName, String password, String phoneNumber, String email,
                            LocalDateTime dueDate, Integer currentWeek, String lastPeriod, Float height) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.dueDate = dueDate;
        this.currentWeek = currentWeek;
        this.lastPeriod = lastPeriod;
        this.height = height;
    }
}

