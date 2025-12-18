package com.example.banking_app.exception;

public class UserAlreadyLinkedToAccountException extends RuntimeException{
    public UserAlreadyLinkedToAccountException(String message){
        super("User is Already linked to this bank account");
    }
}
