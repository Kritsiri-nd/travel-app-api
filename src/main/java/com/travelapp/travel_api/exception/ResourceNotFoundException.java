package com.travelapp.travel_api.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String message, String errorCode) {
        super(HttpStatus.NOT_FOUND, message, errorCode);
    }
}

