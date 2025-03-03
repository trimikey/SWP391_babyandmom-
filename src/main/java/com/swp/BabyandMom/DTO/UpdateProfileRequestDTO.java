package com.swp.BabyandMom.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestDTO {
    private String fullName;
    private String userName;
    private String email;
    private String phone;
}
