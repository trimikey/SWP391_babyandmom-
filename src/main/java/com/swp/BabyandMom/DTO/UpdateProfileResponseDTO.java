package com.swp.BabyandMom.DTO;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateProfileResponseDTO {
    private String name;
    private String email;
    private String phone;
    private String password;

    public UpdateProfileResponseDTO( String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
