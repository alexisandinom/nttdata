package com.nttdata.accountservice.accounts.infrastructure.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepo extends CrudRepository<AccountEntity, UUID> {
    AccountEntity findByNumber(String number);
    List<AccountEntity> findByCustomerIdentification(String identification);
}


