package com.example.restservice.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<String>handleInvalidAccount(InvalidAccountException exception){

        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
