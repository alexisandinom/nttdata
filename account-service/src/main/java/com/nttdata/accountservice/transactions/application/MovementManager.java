package com.nttdata.accountservice.transactions.application;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountRepository;
import com.nttdata.accountservice.config.KafkaEventPublisher;
import com.nttdata.accountservice.transactions.application.dto.TransactionRequestDTO;
import com.nttdata.accountservice.transactions.application.dto.TransactionResponseDTO;
import com.nttdata.accountservice.transactions.domain.Transaction;
import com.nttdata.accountservice.transactions.domain.TransactionId;
import com.nttdata.accountservice.transactions.domain.TransactionRepository;
import com.nttdata.common.events.MovementCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class MovementManager {

    private static final Logger logger = LoggerFactory.getLogger(MovementManager.class);
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaEventPublisher eventPublisher;

    public MovementManager(AccountRepository accountRepository, 
                          TransactionRepository transactionRepository,
                          KafkaEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Mono<TransactionResponseDTO> storeMovement(TransactionRequestDTO dto) {
        logger.info("Storing movement for account: {}, type: {}, value: {}", 
                dto.getAccount(), dto.getType(), dto.getValue());
        return Mono.fromCallable(() -> {
                    Account account = accountRepository.getByNumber(dto.getAccount());
                    Transaction transaction = dto.toTransaction();
                    transaction.setAccount(account);
                    return transactionRepository.save(transaction);
                })
                .doOnNext(transaction -> {
                    MovementCreatedEvent event = new MovementCreatedEvent(
                            transaction.getId().getValue().toString(),
                            transaction.getAccount().getNumber(),
                            transaction.getType().toString(),
                            transaction.getValue().getAmount(),
                            transaction.getBalance().getAmount(),
                            LocalDateTime.now()
                    );
                    eventPublisher.publishMovementCreated(event);
                    logger.info("Published MovementCreatedEvent for movement: {}", transaction.getId());
                })
                .map(TransactionResponseDTO::new);
    }

    public Mono<TransactionResponseDTO> getMovement(String transactionId) {
        logger.info("Getting movement with id: {}", transactionId);
        return Mono.fromCallable(() -> {
                    Transaction transaction = transactionRepository.getById(
                            new TransactionId(java.util.UUID.fromString(transactionId)));
                    return transaction;
                })
                .map(TransactionResponseDTO::new);
    }
}



