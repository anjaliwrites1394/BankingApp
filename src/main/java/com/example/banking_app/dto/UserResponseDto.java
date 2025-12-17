package com.example.banking_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserResponseDto {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email(message = "Invalid Email format")
    private String email;
}
