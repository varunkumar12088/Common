package com.academy.common.exception;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ArgumentException extends RuntimeException{
    private List<FieldError> invalidFields;

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public void addInvalidField(String field, String message) {
        if (this.invalidFields == null) {
            this.invalidFields = new ArrayList<>();
        }
        FieldError fieldError = new FieldError();
        fieldError.setField(field);
        fieldError.setMessage(message);
        this.invalidFields.add(fieldError);
    }

    public void addInvalidField(String field, Object filedValue, String message) {
        if (this.invalidFields == null) {
            this.invalidFields = new ArrayList<>();
        }
        FieldError fieldError = new FieldError();
        fieldError.setField(field);
        fieldError.setMessage(message);
        fieldError.setFieldValue(filedValue);
        this.invalidFields.add(fieldError);
    }

    public List<FieldError> getInvalidFields() {
        return invalidFields;
    }

    public boolean hasInvalidFields() {
        return !CollectionUtils.isEmpty(this.invalidFields);
    }
}
