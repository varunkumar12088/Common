package com.academy.common.exception;

import lombok.Data;

@Data
public class FieldError {

    private String field;
    private Object fieldValue;
    private String message;

}
