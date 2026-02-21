package com.example.account_service.adapters.in;

import com.example.account_service.application.dto.*;
import com.example.account_service.application.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping
    public ResponseEntity<?> newAccount(@Valid @RequestBody AccountRegistrationDTO accountRegistrationDTO) {

        accountService.newAccount(accountRegistrationDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long accountId) {

        AccountResponse accResp = accountService.findById(accountId);

        return ResponseEntity.ok(accResp);
    }

    @PatchMapping("/balance/me")
    public ResponseEntity<?> updateMyBalance(@Valid @RequestBody AddBalanceByAccIdDTO addBalanceByAccIdDTO ) {

        accountService.updateMyBalance(addBalanceByAccIdDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/balance")
    public ResponseEntity<?> updateBalance(@Valid @RequestBody AddBalanceByAccNumDTO addBalanceByAccNumDTO) {

        accountService.updateBalance(addBalanceByAccNumDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/status")
    public ResponseEntity<?> changeAccountStatus(@Valid @RequestBody ChangeAccStatusDTO changeAccStatusDTO) {

        accountService.changeAccountState(changeAccStatusDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
