package com.swp.BabyandMom.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RegisterRequestDTO {
    // Thông tin user hiện tại
    private String fullName;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;


}

