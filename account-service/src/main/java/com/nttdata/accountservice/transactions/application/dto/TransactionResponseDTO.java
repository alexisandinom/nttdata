package com.nttdata.accountservice.transactions.application.dto;

import com.nttdata.accountservice.transactions.domain.Transaction;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionResponseDTO {
    private String account;
    private Long ordinal;
    private TransactionType type;
    private LocalDateTime date;
    private BigDecimal value;
    private BigDecimal balance;

    public TransactionResponseDTO(Transaction transaction) {
        this.account = transaction.getAccount().getNumber();
        this.ordinal = transaction.getOrdinal();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.value = transaction.getValue().getAmount()
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        this.balance = transaction.getBalance().getAmount();
    }
}


