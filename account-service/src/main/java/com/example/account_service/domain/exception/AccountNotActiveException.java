package com.example.account_service.domain.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(String message) {
        super(message);
    }

    public AccountNotActiveException() {
    }

    public AccountNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
