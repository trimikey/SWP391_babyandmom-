package com.swp.BabyandMom.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity handleValidationException(MethodArgumentNotValidException e) {
        String msg = "";
        for(FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            msg += fieldError.getDefaultMessage() + "\n";
        }
        return new ResponseEntity(msg , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleValidation (Exception exception){
        // mỗi khi gặp lỗi này lập tức gọi
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
