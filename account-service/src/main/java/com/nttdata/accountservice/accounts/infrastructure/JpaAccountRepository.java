package com.nttdata.accountservice.accounts.infrastructure;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountRepository;
import com.nttdata.accountservice.accounts.domain.CustomerReference;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountEntity;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountRepo;
import com.nttdata.core.exceptions.InvalidArgumentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaAccountRepository implements AccountRepository {

    private final AccountRepo accountRepo;

    public JpaAccountRepository(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Account getByNumber(String number) throws InvalidArgumentException {
        AccountEntity entity = accountRepo.findByNumber(number);
        if (entity == null) {
            throw new EntityNotFoundException("Account with number " + number + " not found");
        }
        return entity.toDomain();
    }

    @Override
    public List<Account> getByCustomerIdentification(String identification) throws InvalidArgumentException {
        List<AccountEntity> entities = accountRepo.findByCustomerIdentification(identification);
        List<Account> accounts = new ArrayList<>();
        for (AccountEntity entity : entities) {
            accounts.add(entity.toDomain());
        }
        return accounts;
    }

    @Override
    public Account save(Account account) throws InvalidArgumentException {
        AccountEntity entity = accountRepo.findByNumber(account.getNumber());
        if (entity != null) {
            throw new EntityNotFoundException("Account with number " + account.getNumber() + " already exists");
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setNumber(account.getNumber());
        accountEntity.setCustomerIdentification(account.getCustomer().getIdentification());
        accountEntity.setCustomerName(account.getCustomer().getName());
        accountEntity.setStatusAccount(account.getStatusAccount());
        accountEntity.setType(account.getType());
        accountEntity.setInitialBalance(account.getInitialBalance().getAmount());
        accountEntity.setCurrentBalance(account.getInitialBalance().getAmount());
        
        return accountRepo.save(accountEntity).toDomain();
    }

    @Override
    public Account update(Account account) throws InvalidArgumentException {
        Optional<AccountEntity> accountEntity = Optional.ofNullable(accountRepo.findById(account.getId().getValue()))
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        if (accountEntity.isPresent()) {
            accountEntity.get().toEntity(account);
        }
        return accountRepo.save(accountEntity.get()).toDomain();
    }

    @Override
    @Transactional
    public void delete(String number) {
        AccountEntity accountEntity = accountRepo.findByNumber(number);
        if (accountEntity != null) {
            if (accountEntity.getTransactions() != null && !accountEntity.getTransactions().isEmpty()) {
                throw new EntityNotFoundException("Account has transactions and cannot be deleted");
            }
            accountRepo.delete(accountEntity);
        } else {
            throw new EntityNotFoundException("Account not found");
        }
    }
}


