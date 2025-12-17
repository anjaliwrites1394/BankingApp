package com.example.banking_app.exception;

public class UserAlreadyExistsException extends RuntimeException {

    private String fieldName;
    private String fieldValue;

    public UserAlreadyExistsException(String message, String fieldName, String fieldValue) {

        super(String.format("User with $S:$S already exists", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
