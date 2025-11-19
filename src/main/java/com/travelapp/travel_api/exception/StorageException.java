package com.travelapp.travel_api.exception;

import org.springframework.http.HttpStatus;

public class StorageException extends AppException {

    public StorageException(String message, String errorCode) {
        super(HttpStatus.BAD_GATEWAY, message, errorCode);
    }

    public StorageException(String message, String errorCode, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, message, errorCode);
        initCause(cause);
    }
}

