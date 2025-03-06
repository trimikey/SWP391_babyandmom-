package com.swp.BabyandMom.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileResponseDTO {
    private String fullName;
    private String userName;
    private String email;
    private String phone;

    public UpdateProfileResponseDTO(String fullName, String userName, String email, String phone) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
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
