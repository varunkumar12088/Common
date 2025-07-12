package com.academy.common.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private int errorCode;
    private Object details;
    private String timestamp;

    public ErrorResponse(String message, int errorCode, Object details, String timestamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.timestamp = timestamp;
    }

}
