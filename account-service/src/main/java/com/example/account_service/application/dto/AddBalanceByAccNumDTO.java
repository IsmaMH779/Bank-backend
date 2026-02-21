package com.example.account_service.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddBalanceByAccNumDTO(

        @NotNull(message = "Account number field cannot be empty")
        String accNumber,

        @NotNull(message = "Quantity field cannot be empty")
        @Positive(message = "The quantity must be greater than 0")
        BigDecimal quantity
) {
}