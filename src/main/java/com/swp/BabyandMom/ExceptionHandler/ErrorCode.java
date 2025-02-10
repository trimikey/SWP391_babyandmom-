package com.swp.BabyandMom.ExceptionHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {

    ACCOUNT_NOT_VERIFY(1001, "This account has not been verified", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1002,"Email not found, please register account.",HttpStatus.NOT_FOUND),
    EMAIL_PASSWORD_NOT_CORRECT(1003, "Email or password is not correct", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1004, "Invalid token", HttpStatus.BAD_REQUEST),

    ;
    @Getter
    private final Integer code;
    @Setter
    private String message;
    @Getter
    private final HttpStatus httpStatus;

    ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
