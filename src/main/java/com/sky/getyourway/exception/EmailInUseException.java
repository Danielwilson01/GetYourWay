package com.sky.getyourway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class EmailInUseException extends RuntimeException{

    public EmailInUseException(String message) {
        super(message);
    }
}
