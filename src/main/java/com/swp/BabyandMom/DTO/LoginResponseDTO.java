package com.swp.BabyandMom.DTO;

public class LoginResponseDTO {

    private String message;
    private String error;
    private String accessToken;
    private String refreshToken;

    public LoginResponseDTO(String message, String error, String accessToken, String refreshToken) {
        this.message = message;
        this.error = error;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
}
