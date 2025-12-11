package com.nttdata.accountservice.transactions.domain;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {
    private TransactionId id;
    private Account account;
    private Long ordinal;
    private TransactionType type;
    private LocalDateTime date;
    private Money value;
    private Money balance;

    public static Transaction create(final TransactionId transactionId, final Account account, final Long ordinal, final TransactionType type,
                                     final LocalDateTime date, final Money value, final Money balance) throws InvalidArgumentException {

        if (value.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidArgumentException("amount", "cannot be less or equal than zero");
        }

        return new Transaction(transactionId, account, ordinal, type, date, value, balance);
    }

    public Transaction setAccount(Account account){
        this.account = account;
        return this;
    }
}


