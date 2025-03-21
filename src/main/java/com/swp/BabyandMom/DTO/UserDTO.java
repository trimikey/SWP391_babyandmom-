package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.Enum.UserStatusEnum;
import lombok.*;

@Getter
@Setter

@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private UserStatusEnum status;
    private RoleType role;

    public UserDTO(RoleType role,Long id, String fullName, String userName, String email, String phone, UserStatusEnum status) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
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

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }
}
