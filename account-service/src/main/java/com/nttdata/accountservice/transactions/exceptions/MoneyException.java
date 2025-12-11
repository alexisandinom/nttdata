package com.nttdata.accountservice.transactions.exceptions;

import com.nttdata.core.exceptions.DomainException;
public class MoneyException extends DomainException {

  public MoneyException(String message) {
    super(message);
  }
}


