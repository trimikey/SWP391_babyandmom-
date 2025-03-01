package com.swp.BabyandMom.DTO;

import com.swp.BabyandMom.Entity.Enum.RoleType;

public class LoginResponseDTO {

    private String message;
    private String error;
    private String accessToken;
    private String refreshToken;
    private RoleType role;

    public LoginResponseDTO(String message, String error, String accessToken, String refreshToken, RoleType role) {
        this.message = message;
        this.error = error;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
