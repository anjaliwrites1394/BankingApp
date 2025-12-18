package com.example.banking_app.exception;

public class MaxUsersInAccountReachedException extends RuntimeException{
    public MaxUsersInAccountReachedException(String message) {
        super("Account cannot have more than 3 users.");
    }
}
