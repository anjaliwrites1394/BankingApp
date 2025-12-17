package com.example.banking_app.exception;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(String message) {
        super("You are not authorized to access this account. Please recheck your username or password");
    }
}
