package com.nttdata.accountservice.reports.application;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountRepository;
import com.nttdata.accountservice.reports.application.dto.AccountReportDTO;
import com.nttdata.accountservice.reports.application.dto.ReportRequestDTO;
import com.nttdata.accountservice.reports.application.dto.ReportResponseDTO;
import com.nttdata.accountservice.transactions.application.dto.TransactionResponseDTO;
import com.nttdata.accountservice.transactions.domain.TransactionRepository;
import com.nttdata.core.exceptions.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ReportManager {

    private static final Logger logger = LoggerFactory.getLogger(ReportManager.class);
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public ReportManager(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<ReportResponseDTO> generateReport(ReportRequestDTO dto) {
        logger.info("Generating report for client: {}, from: {} to: {}", 
                dto.getClientId(), dto.getStartDate(), dto.getEndDate());
        
        return Mono.fromCallable(() -> accountRepository.getByCustomerIdentification(dto.getClientId()))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Mono.error(new InvalidArgumentException("clientId", 
                        "No accounts found for client " + dto.getClientId())))
                .flatMap(account -> {
                    return Mono.fromCallable(() -> 
                            transactionRepository.getBetweenDates(account, dto.getStartDate(), dto.getEndDate()))
                            .flatMapMany(Flux::fromIterable)
                            .map(TransactionResponseDTO::new)
                            .collectList()
                            .map(movements -> new AccountReportDTO(
                                    account.getNumber(),
                                    account.getType(),
                                    account.getInitialBalance().getAmount(),
                                    account.getCurrentBalance().getAmount(),
                                    account.getStatusAccount(),
                                    movements
                            ));
                })
                .collectList()
                .flatMap(accountReports -> {
                    if (accountReports.isEmpty()) {
                        return Mono.error(new InvalidArgumentException("clientId", 
                                "No accounts found for client " + dto.getClientId()));
                    }
                    
                    // Get customer info from first account
                    return Mono.fromCallable(() -> {
                        List<Account> accounts = accountRepository.getByCustomerIdentification(dto.getClientId());
                        if (accounts.isEmpty()) {
                            throw new InvalidArgumentException("clientId", 
                                    "No accounts found for client " + dto.getClientId());
                        }
                        Account firstAccount = accounts.get(0);
                        String customerName = firstAccount.getCustomer().getName();
                        String identification = firstAccount.getCustomer().getIdentification();
                        return new ReportResponseDTO(identification, customerName, accountReports);
                    });
                });
    }
}


