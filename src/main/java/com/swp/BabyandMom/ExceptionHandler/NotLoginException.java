package com.swp.BabyandMom.ExceptionHandler;


public class NotLoginException extends RuntimeException{

    public NotLoginException(String message){
        super(message);
    }
}
