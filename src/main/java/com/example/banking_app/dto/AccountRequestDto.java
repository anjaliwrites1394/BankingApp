package com.example.banking_app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountRequestDto {
    @NotNull(message = "Current balance must not be empty")
    @Positive(message = "Current balance must be greater than zero")
    private Double currentBal;
    @NotEmpty
    private String accountType;
}
