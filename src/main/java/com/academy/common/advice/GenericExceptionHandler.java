package com.academy.common.advice;

import com.academy.common.domain.ErrorResponse;
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
        ErrorResponse response = ErrorResponse.builder().errorCode("500")
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .timestamp(java.time.LocalDateTime.now().toString()).build();

        return ResponseEntity.status(500).body(response);
    }

}
