package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.UserStatusEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private UserStatusEnum status;
}
