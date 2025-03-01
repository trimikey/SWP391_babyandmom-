package com.swp.BabyandMom.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {
    private String email;
    private String code;
    private String newPassword;
    private String confirmedNewPassword;



}
