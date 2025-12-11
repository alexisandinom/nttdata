package com.nttdata.accountservice.accounts.application.dto;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountType;
import com.nttdata.accountservice.accounts.infrastructure.jpa.StatusAccount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountResponseDTO{
    private String identification;
    private String customerName;
    private AccountType type;
    private String number;
    private BigDecimal initialBalance;
    private StatusAccount statusAccount = StatusAccount.ACTIVE;

    public AccountResponseDTO(Account account) {
        this.identification = account.getCustomer().getIdentification();
        this.customerName = account.getCustomer().getName();
        this.type = account.getType();
        this.number = account.getNumber();
        this.initialBalance = account.getInitialBalance().getAmount()
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        this.statusAccount = account.getStatusAccount();
    }
}


