package com.academy.common.advice;

import com.academy.common.dto.ErrorResponse;
import com.academy.common.exception.ArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleException(Throwable e) {
        LOGGER.error("An error occurred", e);
        ErrorResponse response = ErrorResponse.builder().errorCode(500)
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .timestamp(java.time.LocalDateTime.now().toString()).build();

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(ArgumentException.class)
    public ResponseEntity<?> handleArgumentException(ArgumentException e) {
        LOGGER.error("Argument exception occurred", e);
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(400)
                .message("Invalid arguments provided")
                .details(e.getInvalidFields() == null ? e.getMessage() : e.getInvalidFields())
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("IllegalArgumentException occurred ", e);
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(400)
                .message(e.getMessage())
                .details(e.getMessage())
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.status(400).body(response);
    }

}
