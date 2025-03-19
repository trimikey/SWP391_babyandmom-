package com.swp.BabyandMom.DTO;

import lombok.*;

@Getter
@Setter

@NoArgsConstructor
public class GetProfileResponseDTO {
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private Long id;

    public GetProfileResponseDTO(Long id,String fullName, String userName, String email, String phone) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
