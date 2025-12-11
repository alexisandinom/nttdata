package com.nttdata.accountservice.transactions.infrastructure.jpa;

import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountEntity;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.core.infraestructure.jpa.AbstractEntity;
import com.nttdata.accountservice.transactions.domain.Money;
import com.nttdata.accountservice.transactions.domain.Transaction;
import com.nttdata.accountservice.transactions.domain.TransactionId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class TransactionEntity extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private AccountEntity account;

    @Column(name = "ordinal", nullable = false)
    private long ordinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public Transaction toDomain() throws InvalidArgumentException {
        return Transaction.create(new TransactionId(this.getId()), this.account.toDomain(), this.ordinal, this.type, this.date, new Money(this.getValue()), new Money(this.getBalance()));
    }

    public TransactionEntity toEntity(Transaction transaction, AccountEntity accountEntity) {
        this.setId(Objects.nonNull(transaction.getId()) ? transaction.getId().getValue() : null);
        this.account = accountEntity;
        this.ordinal = transaction.getOrdinal();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.value = transaction.getValue().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.balance = transaction.getBalance().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        return this;
    }
}

