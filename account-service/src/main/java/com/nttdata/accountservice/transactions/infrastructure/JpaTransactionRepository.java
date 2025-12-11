package com.nttdata.accountservice.transactions.infrastructure;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountEntity;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountRepo;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.accountservice.transactions.domain.Transaction;
import com.nttdata.accountservice.transactions.domain.TransactionId;
import com.nttdata.accountservice.transactions.domain.TransactionRepository;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionEntity;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionRepo;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionType;
import com.nttdata.accountservice.transactions.exceptions.InsufficientBalanceException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaTransactionRepository implements TransactionRepository {

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    public JpaTransactionRepository(AccountRepo accountRepo, TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public Transaction getById(TransactionId transactionId) throws InvalidArgumentException {
        Optional<TransactionEntity> entity = transactionRepo.findById(transactionId.getValue());
        if (!entity.isPresent()) {
            throw new EntityNotFoundException("Transaction with id " + transactionId + " not found");
        }
        return entity.get().toDomain();
    }


    @Override
    public List<Transaction> getBetweenDates(Account account, LocalDate from, LocalDate to) throws InvalidArgumentException {
        AccountEntity accountEntity = accountRepo.findByNumber(account.getNumber());
        List<TransactionEntity> transactions = transactionRepo.findByAccountAndDateBetween(accountEntity, from.atStartOfDay(), to.atTime(LocalTime.MAX));
        List<Transaction> result = new ArrayList<>();
        for (TransactionEntity entity : transactions) {
            try {
                result.add(convertToDomain(entity));
            } catch (InvalidArgumentException e) {
                throw new InvalidArgumentException("Error converting transaction: " + e.getMessage());
            }
        }
        return result;
    }

    private Transaction convertToDomain(TransactionEntity entity) throws InvalidArgumentException {
        return entity.toDomain();
    }

    @Override
    public Transaction save(Transaction transaction) throws InvalidArgumentException, InsufficientBalanceException {
        long currentSequence = 1;
        BigDecimal balance = BigDecimal.ZERO;

        AccountEntity accountEntity = accountRepo.findByNumber(transaction.getAccount().getNumber());
        if (accountEntity == null) {
            throw new EntityNotFoundException("Account with number " + transaction.getAccount().getNumber() + " not found");
        }

        TransactionEntity entity = transactionRepo.findTopByAccountCustomerPersonIdentificationOrderByOrdinalDesc
                (accountEntity.getCustomerIdentification(), accountEntity.getNumber());
        if (entity != null) {
            // Hay transacciones previas: usar el balance de la última transacción
            currentSequence = currentSequence + entity.getOrdinal();
            if (transaction.getType().equals(TransactionType.DEPOSIT)) {
                balance = entity.getBalance().add(transaction.getValue().getAmount());
            } else {
                balance = entity.getBalance().subtract(transaction.getValue().getAmount());
            }
        } else {
            // Primera transacción: usar el balance actual de la cuenta como base
            BigDecimal accountCurrentBalance = accountEntity.getCurrentBalance();
            if (transaction.getType().equals(TransactionType.DEPOSIT)) {
                balance = accountCurrentBalance.add(transaction.getValue().getAmount());
            } else {
                balance = accountCurrentBalance.subtract(transaction.getValue().getAmount());
            }
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(transaction.getAccount().getNumber(), "Insufficient balance");
        }

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setDate(LocalDateTime.now());
        transactionEntity.setOrdinal(currentSequence);
        transactionEntity.setType(transaction.getType());
        transactionEntity.setValue(transaction.getValue().getAmount());
        transactionEntity.setBalance(balance);

        accountEntity.setCurrentBalance(balance);
        accountRepo.save(accountEntity);

        final Transaction transactionSaved = transactionRepo.save(transactionEntity).toDomain();

        return transactionSaved;
    }
}


