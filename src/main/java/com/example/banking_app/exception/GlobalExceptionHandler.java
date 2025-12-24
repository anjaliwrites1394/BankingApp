package com.example.banking_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> UserAlreadyExistsException (
            UserAlreadyExistsException userAlreadyExistsException, WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                userAlreadyExistsException.getMessage(),
                webRequest.getDescription(false),
                "USER_ALREADY_EXISTS");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException, WebRequest webRequest){
        return new ResponseEntity<>(new ErrorDetails(
                LocalDateTime.now(),
                resourceNotFoundException.getMessage(),
                webRequest.getDescription(false),
                "RESOURCE_NOT_FOUND"
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<ErrorDetails> userNotAuthorizedException(
            UserNotAuthorizedException userNotAuthorizedException, WebRequest webRequest){

        return  new ResponseEntity<>(new ErrorDetails(
                LocalDateTime.now(),
                userNotAuthorizedException.getMessage(),
                webRequest.getDescription(false),
                "NOT_AUTHORIZED"), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(LowBalanceException.class)
    public ResponseEntity<ErrorDetails> lowBalanceException(
            LowBalanceException lowBalanceException, WebRequest webRequest){
        return new ResponseEntity(new ErrorDetails(
                LocalDateTime.now(),
                lowBalanceException.getMessage(),
                webRequest.getDescription(false),
                "LOW_BALANCE"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDepositAmountException.class)
    public ResponseEntity<ErrorDetails> UserAlreadyExistsException (
            InvalidDepositAmountException invalidDepositAmountException, WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                invalidDepositAmountException.getMessage(),
                webRequest.getDescription(false),
                "INVALID_DEPOSIT_AMOUNT");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUsersInAccountReachedException.class)
    public ResponseEntity<ErrorDetails> MaxUsersInAccountReachedException (
            MaxUsersInAccountReachedException maxUsersInAccountReachedException, WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                maxUsersInAccountReachedException.getMessage(),
                webRequest.getDescription(false),
                "MAX_LIMIT_REACHED");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyLinkedToAccountException.class)
    public ResponseEntity<ErrorDetails> UserAlreadyLinkedToAccountException
            (UserAlreadyLinkedToAccountException userAlreadyLinkedToAccountException,
                                               WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                userAlreadyLinkedToAccountException.getMessage(),
                webRequest.getDescription(false),
                "MAX_LIMIT_REACHED");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankAccountDoesNotExistException.class)
    public ResponseEntity<ErrorDetails> BankAccountDoesNotExist(
            BankAccountDoesNotExistException bankAccountDoesNotExistException, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                bankAccountDoesNotExistException.getMessage(),
                webRequest.getDescription(false),
                "BANK_ACCOUNT_NUMBER_INVALID");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
