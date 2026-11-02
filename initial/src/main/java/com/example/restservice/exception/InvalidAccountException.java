package com.example.restservice.exception;

public class InvalidAccountException extends RuntimeException{

    public InvalidAccountException(String message) {
        super(message);
    }
}
