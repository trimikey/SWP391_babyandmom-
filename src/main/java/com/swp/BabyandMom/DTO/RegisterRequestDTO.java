package com.swp.BabyandMom.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestDTO {
    private String name;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;

    public RegisterRequestDTO(String name, String userName, String password ,String phoneNumber, String email) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }
}

