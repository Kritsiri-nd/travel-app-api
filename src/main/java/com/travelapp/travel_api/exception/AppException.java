package com.travelapp.travel_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected AppException(HttpStatus status, String message, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}

