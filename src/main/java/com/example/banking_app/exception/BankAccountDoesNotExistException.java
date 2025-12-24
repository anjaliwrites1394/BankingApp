package com.example.banking_app.exception;

public class BankAccountDoesNotExistException extends RuntimeException {

    Long accountNo;
    public BankAccountDoesNotExistException(String message, Long accountNo) {
        super(String.format("Bank Account with this number $ does not exist", accountNo));
        accountNo = this.accountNo;
    }
}
