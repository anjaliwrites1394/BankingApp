package com.example.banking_app.dto;

import com.example.banking_app.entity.Users;
import com.example.banking_app.entity.UsersBankAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBankAccountResponseDto {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email(message = "Invalid Email format")
    private String email;
    @NotEmpty
    private String username;
    @NotEmpty
    private Double currentBal;
    @NotEmpty
    private String accountType;
    @NotEmpty
    private List<Long> bankAccountNumbers;


}
