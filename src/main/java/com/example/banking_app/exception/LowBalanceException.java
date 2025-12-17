package com.example.banking_app.exception;

public class LowBalanceException extends RuntimeException {
    public LowBalanceException(String message) {
        super("Cannot withdraw: Low Balance in your account.");
    }
}
