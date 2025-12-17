package com.example.banking_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBankAccountRequestDto {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email(message = "Invalid Email format")
    private String email;
    @NotEmpty
    private String username;
    @NotEmpty
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull(message = "Current balance must not be empty")
    @Positive(message = "Current balance must be greater than zero")
    private Double currentBal;
    @NotEmpty
    private String accountType;


}
