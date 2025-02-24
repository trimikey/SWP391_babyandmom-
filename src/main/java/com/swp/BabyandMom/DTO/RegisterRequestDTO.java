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


}

