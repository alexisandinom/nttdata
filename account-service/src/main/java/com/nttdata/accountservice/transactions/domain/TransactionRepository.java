package com.nttdata.accountservice.transactions.domain;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.transactions.exceptions.InsufficientBalanceException;
import com.nttdata.core.exceptions.InvalidArgumentException;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {
    Transaction getById(TransactionId transactionId) throws InvalidArgumentException;
    List<Transaction> getBetweenDates(Account account, LocalDate from, LocalDate to) throws InvalidArgumentException;
    Transaction save(Transaction transaction) throws InvalidArgumentException, InsufficientBalanceException;
}


