package com.example.account_service.application.dto;

import com.example.account_service.domain.util.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountRegistrationDTO(
        @NotNull(message = "User ID field cannot be empty.")
        Long userId,

        @NotNull(message = "Currency field cannot be empty")
        Currency currency,

        @NotNull(message = "Initial balance field cannot be empty")
        @PositiveOrZero(message = "Amount cannot be negative")
        BigDecimal initialBalance
) {
}
