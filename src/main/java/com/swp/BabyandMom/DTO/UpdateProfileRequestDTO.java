package com.swp.BabyandMom.DTO;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
}
