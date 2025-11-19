package com.travelapp.travel_api.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException {

    public BadRequestException(String message, String errorCode) {
        super(HttpStatus.BAD_REQUEST, message, errorCode);
    }
}

