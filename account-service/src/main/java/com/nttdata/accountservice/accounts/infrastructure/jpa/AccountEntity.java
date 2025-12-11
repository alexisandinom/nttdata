package com.nttdata.accountservice.accounts.infrastructure.jpa;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountId;
import com.nttdata.accountservice.accounts.domain.CustomerReference;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.core.infraestructure.jpa.AbstractEntity;
import com.nttdata.accountservice.transactions.domain.Money;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(name = "UK_ACCOUNT_NUMBER", columnNames = {"number"})
})
public class AccountEntity extends AbstractEntity {

    @Column(name = "customer_identification", nullable = false, length = 13)
    private String customerIdentification;

    @Column(name = "customer_name", length = 75)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccountType type;

    @Column(name = "number", nullable = false, length = 15)
    private String number;

    @Column(name = "initial_balance", nullable = false)
    private BigDecimal initialBalance;

    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_account", nullable = false)
    private StatusAccount statusAccount;

    @OneToMany(mappedBy = "account")
    private Collection<TransactionEntity> transactions;

    public Account toDomain() throws InvalidArgumentException {
        CustomerReference customer = new CustomerReference(
                this.customerIdentification, // Using identification as customerId
                this.customerIdentification,
                this.customerName != null ? this.customerName : "",
                true // Assuming active if exists
        );
        return Account.create(
                new AccountId(this.getId()),
                customer,
                this.type,
                this.number,
                new Money(this.getInitialBalance()),
                new Money(this.getCurrentBalance()),
                this.statusAccount
        );
    }

    public AccountEntity toEntity(Account account) {
        this.setId(Objects.nonNull(account.getId()) ? account.getId().getValue() : null);
        this.customerIdentification = account.getCustomer().getIdentification();
        this.customerName = account.getCustomer().getName();
        this.type = account.getType();
        this.number = account.getNumber();
        this.initialBalance = account.getInitialBalance().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.currentBalance = account.getCurrentBalance().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.statusAccount = account.getStatusAccount();
        return this;
    }
}


