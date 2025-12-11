package com.nttdata.accountservice.accounts.domain;

import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountType;
import com.nttdata.accountservice.accounts.infrastructure.jpa.StatusAccount;
import com.nttdata.accountservice.accounts.domain.CustomerReference;
import com.nttdata.accountservice.transactions.domain.Money;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
    private AccountId id;
    private CustomerReference customer;
    private AccountType type;
    private String number;
    private Money initialBalance;
    private Money currentBalance;
    private StatusAccount statusAccount;
    private Collection<TransactionEntity> transactions;

    public static Account create(final AccountId accountId, final CustomerReference customer, final AccountType type, final String number,
                                 final Money initialBalance, final Money currentBalance, final StatusAccount statusAccount) {
        return new Account(accountId, customer, type, number, initialBalance, currentBalance, statusAccount, new ArrayList<>());
    }

    public Account updateBalance(final Money newBalance) {
        this.initialBalance = newBalance;
        return this;
    }

    public Account setCustomer(final CustomerReference customer) {
        this.customer = customer;
        return this;
    }

    public Account updateStatus(final StatusAccount newStatusAccount) {
        this.statusAccount = newStatusAccount;
        return this;
    }

    public Account addTransaction(final TransactionEntity newTransaction) {
        this.transactions.add(newTransaction);
        return this;
    }
}


