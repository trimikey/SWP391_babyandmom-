package com.swp.BabyandMom.ExceptionHandler;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
