package com.example.account_service.domain.model;

import com.example.account_service.domain.util.AccountStatus;
import com.example.account_service.domain.util.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String accountNumber;

    private Currency currency;

    private BigDecimal balance;

    private AccountStatus status;

    private LocalDateTime createdAt;
}
