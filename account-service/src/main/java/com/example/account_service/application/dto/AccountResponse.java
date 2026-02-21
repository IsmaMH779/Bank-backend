package com.example.account_service.application.dto;

import com.example.account_service.domain.util.AccountStatus;
import com.example.account_service.domain.util.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        String accountNumber,
        Currency currency,
        BigDecimal balance,
        AccountStatus status
) {
}
