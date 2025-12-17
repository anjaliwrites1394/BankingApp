package com.example.banking_app.serviceImpl;

import com.example.banking_app.dto.*;
import com.example.banking_app.entity.Users;
import jakarta.validation.Valid;

public interface BankAccountService
{

//UserBankAccountResponseDto addAccount(UserBankAccountRequestDto account);
UserBankAccountResponseDto getAccountDetails(Long bankAccountNo);
UserBankAccountResponseDto depositAmount(Long bankAccountNo, double depositAmount);
UserBankAccountResponseDto withdrawAmount(Long bankAccountNo, double withdrawAmount);
String deleteAccount(Long bankAccountNo);
String verify(Users user);
UserResponseDto registerUser(@Valid UserRequestDto user);
UserBankAccountResponseDto createAccount(Long userId, @Valid AccountRequestDto account);
}