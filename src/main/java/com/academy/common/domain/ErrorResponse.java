package com.academy.common.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private String errorCode;
    private String details;
    private String timestamp;

    public ErrorResponse(String message, String errorCode, String details, String timestamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.timestamp = timestamp;
    }

}
