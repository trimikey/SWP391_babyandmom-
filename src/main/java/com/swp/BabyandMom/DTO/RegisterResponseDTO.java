package com.swp.BabyandMom.DTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterResponseDTO {
    private Long id;
    private String name;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
    private String message;


    public RegisterResponseDTO(Long id, String name, String userName ,String phoneNumber, String email ,String message) {

        this.id = id;
        this.name = name;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.message = message;


    }



}
