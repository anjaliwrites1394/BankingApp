package com.example.banking_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email(message = "Invalid Email format")
    private String email;
}
