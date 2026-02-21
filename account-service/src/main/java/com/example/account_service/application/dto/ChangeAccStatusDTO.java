package com.example.account_service.application.dto;

import com.example.account_service.domain.util.AccountStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeAccStatusDTO(

        @NotNull(message = "Account number field cannot be empty")
        String accNumber,

        @NotNull(message = "Status field cannot be empty")
        AccountStatus status

) {
}
