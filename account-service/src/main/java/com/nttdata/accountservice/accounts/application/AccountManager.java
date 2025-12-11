package com.nttdata.accountservice.accounts.application;

import com.nttdata.accountservice.accounts.application.dto.AccountRequestDTO;
import com.nttdata.accountservice.accounts.application.dto.AccountResponseDTO;
import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountRepository;
import com.nttdata.accountservice.config.CustomerService;
import com.nttdata.accountservice.config.KafkaEventPublisher;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.common.events.AccountCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class AccountManager {

    private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final KafkaEventPublisher eventPublisher;

    public AccountManager(AccountRepository accountRepository, 
                         CustomerService customerService,
                         KafkaEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.eventPublisher = eventPublisher;
    }

    public Mono<AccountResponseDTO> storeAccount(AccountRequestDTO dto) {
        logger.info("Storing account with number: {} for customer: {}", dto.getNumber(), dto.getIdentification());
        return customerService.getCustomerByIdentification(dto.getIdentification())
                .flatMap(customer -> Mono.fromCallable(() -> {
                            Account account = dto.toAccount();
                            account = account.setCustomer(customer);
                            return accountRepository.save(account);
                        }))
                .doOnNext(account -> {
                    AccountCreatedEvent event = new AccountCreatedEvent(
                            account.getId().getValue().toString(),
                            account.getNumber(),
                            account.getCustomer().getIdentification(),
                            account.getInitialBalance().getAmount(),
                            LocalDateTime.now()
                    );
                    eventPublisher.publishAccountCreated(event);
                    logger.info("Published AccountCreatedEvent for account: {}", account.getId());
                })
                .map(AccountResponseDTO::new);
    }

    public Mono<AccountResponseDTO> updateStatusAccount(AccountRequestDTO dto) {
        logger.info("Updating account status for number: {}", dto.getNumber());
        return Mono.fromCallable(() -> {
                    Account account = accountRepository.getByNumber(dto.getNumber());
                    account.updateStatus(dto.getStatusAccount());
                    return accountRepository.update(account);
                })
                .map(AccountResponseDTO::new);
    }

    public Mono<AccountResponseDTO> getAccount(String number) {
        logger.info("Getting account with number: {}", number);
        return Mono.fromCallable(() -> {
                    Account account = accountRepository.getByNumber(number);
                    return account;
                })
                .map(AccountResponseDTO::new);
    }

    public Mono<Void> deleteAccount(String number) {
        logger.info("Deleting account with number: {}", number);
        return Mono.fromRunnable(() -> {
                    accountRepository.delete(number);
                });
    }
}


