package com.travelapp.travel_api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Value
@Builder
public class ErrorResponse {

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);

    int status;
    String error;
    String message;
    String path;
    String errorCode;
}

