package com.nttdata.accountservice.transactions.exceptions;

import com.nttdata.core.exceptions.DomainException;

public class InsufficientBalanceException extends DomainException {
    
    public InsufficientBalanceException(String message) {
        super(message);
    }
    
    public InsufficientBalanceException(String accountNumber, String message) {
        super("Insufficient balance in account " + accountNumber + ": " + message);
    }
}



