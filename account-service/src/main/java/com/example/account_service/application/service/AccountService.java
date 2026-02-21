package com.example.account_service.application.service;

import com.example.account_service.application.dto.*;
import com.example.account_service.domain.util.AccountStatus;

import java.math.BigDecimal;

public interface AccountService {
    void newAccount(AccountRegistrationDTO accountRegistration);
    AccountResponse findById(Long accountId);
    void updateMyBalance(AddBalanceByAccIdDTO addBalanceByAccIdDto);
    void updateBalance(AddBalanceByAccNumDTO addBalanceByAccNumDTO);
    void changeAccountState(ChangeAccStatusDTO accStatusDTO);
}
