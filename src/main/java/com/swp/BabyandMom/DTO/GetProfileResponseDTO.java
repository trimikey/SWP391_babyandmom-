package com.swp.BabyandMom.DTO;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GetProfileResponseDTO {
    private String name;
    private String email;
    private String phone;
    private RoleType accountRole;

    public GetProfileResponseDTO( String name, String email, String phone, RoleType accountRole) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accountRole = accountRole;
    }

}
