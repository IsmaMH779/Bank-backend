package com.example.account_service.application.service.impl;

import com.example.account_service.adapters.out.AccountRepository;
import com.example.account_service.application.dto.*;
import com.example.account_service.application.service.AccountService;
import com.example.account_service.domain.exception.AccountNotActiveException;
import com.example.account_service.domain.exception.ObjectNotFoundException;
import com.example.account_service.domain.model.Account;
import com.example.account_service.domain.util.AccountNumberGen;
import com.example.account_service.domain.util.AccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void newAccount(AccountRegistrationDTO accountRegistration) {

        String accountNumber = AccountNumberGen.generate();

        accountRepository.findByAccountNumber(accountNumber)
                .ifPresent(acc -> {
                    throw new ObjectNotFoundException("Account with account number " + acc.getAccountNumber() + " exist");
                });

        Account newAcc = Account.builder()
                .userId(accountRegistration.userId())
                .accountNumber(accountNumber)
                .currency(accountRegistration.currency())
                .balance(accountRegistration.initialBalance())
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        accountRepository.save(newAcc);

    }

    @Override
    public AccountResponse findById(Long accountId) {

        Account accFromDb = accountRepository.findById(accountId)
                .orElseThrow(() -> new ObjectNotFoundException("Account do not exist"));

        return AccountResponse.builder()
                .accountNumber(accFromDb.getAccountNumber())
                .currency(accFromDb.getCurrency())
                .balance(accFromDb.getBalance())
                .status(accFromDb.getStatus())
                .build();
    }

    @Override
    public void updateMyBalance(AddBalanceByAccIdDTO addBalanceDTO) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("user_id");

        Account account = accountRepository.findById(addBalanceDTO.accId())
                .orElseThrow(() -> new ObjectNotFoundException("Account with id: " + addBalanceDTO.accId() + " does not exist"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("Account with id: " + account.getId() + " is not active");
        }

        if (!Objects.equals(account.getUserId(), userId)) {
            throw new AccessDeniedException("You are not allowed to modify this account");
        }

        addBalance(account, addBalanceDTO.quantity());

    }

    @Override
    public void updateBalance(AddBalanceByAccNumDTO addBalanceDTO) {


        Account account = accountRepository.findByAccountNumber(addBalanceDTO.accNumber())
                .orElseThrow(() -> new ObjectNotFoundException("Account with number: " + addBalanceDTO + " does not exist"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("Account with id: " + account.getId() + " is not active");
        }

        addBalance(account, addBalanceDTO.quantity());
    }

    @Override
    public void changeAccountState(ChangeAccStatusDTO accStatusDTO) {

        Account account = accountRepository.findByAccountNumber(accStatusDTO.accNumber())
                .orElseThrow(() -> new ObjectNotFoundException("Account with number: " + accStatusDTO + " does not exist"));

        account.setStatus(accStatusDTO.status());

        accountRepository.save(account);
    }

    private void addBalance(Account account, BigDecimal addBalanceDTO) {
        BigDecimal updatedBalance = account.getBalance().add(addBalanceDTO);

        account.setBalance(updatedBalance);

        accountRepository.save(account);
    }
}
