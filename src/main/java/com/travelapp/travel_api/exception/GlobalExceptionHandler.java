package com.travelapp.travel_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(400, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(404, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(404).body(body);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorage(StorageException ex, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(502, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(502).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        int code = ex.getStatusCode().value();
        String message = ex.getReason() != null ? ex.getReason() : "error";
        ErrorResponse body = new ErrorResponse(code, message, request.getRequestURI());
        return ResponseEntity.status(code).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(500, "something went wrong", request.getRequestURI());
        return ResponseEntity.status(500).body(body);
    }
}
