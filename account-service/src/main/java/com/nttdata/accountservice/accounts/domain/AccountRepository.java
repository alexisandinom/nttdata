package com.nttdata.accountservice.accounts.domain;

import com.nttdata.core.exceptions.InvalidArgumentException;

import java.util.List;

public interface AccountRepository {
    Account getByNumber (String number) throws InvalidArgumentException;
    List<Account> getByCustomerIdentification(String identification) throws InvalidArgumentException;
    Account save (Account account) throws InvalidArgumentException;
    Account update (Account account) throws InvalidArgumentException;
    void delete (String number);
}


