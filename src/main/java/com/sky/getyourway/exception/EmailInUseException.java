package com.sky.getyourway.exception;

public class EmailInUseException extends RuntimeException{

    public EmailInUseException(String message) {
        super(message);
    }
}