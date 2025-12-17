package com.example.banking_app.exception;

public class InvalidDepositAmountException extends RuntimeException {
    public InvalidDepositAmountException(String message) {
        super("Invalid Deposit Amount: Amount should be equal to or more than 500");
    }
}
