package com.example.banking_app.exception;

public class ResourceNotFoundException extends RuntimeException {

    String fieldName;
    String fieldValue;

    public ResourceNotFoundException(String message, String fieldName, String fieldValue) {

        super(String.format("$S with value $S not found", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
