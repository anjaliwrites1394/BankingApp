package com.example.banking_app.controller;

import com.example.banking_app.dto.*;
import com.example.banking_app.entity.Users;
import com.example.banking_app.entity.UsersBankAccount;
import com.example.banking_app.exception.InvalidDepositAmountException;
import com.example.banking_app.serviceImpl.BankAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/icici/account")
public class BankAccountController {

    BankAccountService bankAccountService;

//    @PostMapping("/register")
//    public ResponseEntity<UserBankAccountResponseDto> addAccount(@RequestBody @Valid UserBankAccountRequestDto account){
//        return ResponseEntity.ok(bankAccountService.addAccount(account));
//    }

    @PostMapping("/registerUser")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto user){
        return ResponseEntity.ok(bankAccountService.registerUser(user));
    }

    @PostMapping("/createAccount/{userId}")
    public ResponseEntity<UserBankAccountResponseDto> createAccount(@PathVariable Long userId, @RequestBody @Valid AccountRequestDto account){
        return ResponseEntity.ok(bankAccountService.createAccount(userId, account));
    }

    @PutMapping("/linkUserAndAccount")
    public ResponseEntity<UserBankAccountResponseDto> linkUserAndAccount(@PathVariable Long userId, @PathVariable Long bankAccountNo){
        return ResponseEntity.ok(bankAccountService.linkUserAndAccount(userId, bankAccountNo));
    }

    @GetMapping("/{bankAccountNo}")
    public ResponseEntity<UserBankAccountResponseDto> getAccount(@PathVariable Long bankAccountNo){
        return ResponseEntity.ok(bankAccountService.getAccountDetails(bankAccountNo));
    }

    @PutMapping("/{bankAccountNo}/{depositAmount}")
    public ResponseEntity<UserBankAccountResponseDto> depositAmount(@PathVariable Long bankAccountNo, @PathVariable Double depositAmount){
        if(depositAmount<500.0)
            throw new InvalidDepositAmountException("Invalid Deposit Amount");
        return ResponseEntity.ok( bankAccountService.depositAmount(
                bankAccountNo,
                depositAmount
        ));
    }

    @PutMapping("withdraw/{bankAccountNo}/{withdrawAmount}")
    public ResponseEntity<UserBankAccountResponseDto> withdrawAmount(@PathVariable Long bankAccountNo, @PathVariable Double withdrawAmount){
        return ResponseEntity.ok(bankAccountService.withdrawAmount(bankAccountNo, withdrawAmount));
    }

    @DeleteMapping("deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        return ResponseEntity.ok(bankAccountService.deleteUser(userId));
    }

    @DeleteMapping("deleteAccount/{bankAccountNo}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long bankAccountNo){
        return ResponseEntity.ok(bankAccountService.deleteAccount(bankAccountNo));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return ResponseEntity.ok(bankAccountService.verify(user));
    }

    @PostMapping
    public ResponseEntity<String> transferFund(
            @PathVariable Long senderAccountNo, @PathVariable Long receiverAccountNo, @PathVariable Double amount){
        return ResponseEntity.ok(bankAccountService.transferFund(senderAccountNo, receiverAccountNo, amount));
    }



}
