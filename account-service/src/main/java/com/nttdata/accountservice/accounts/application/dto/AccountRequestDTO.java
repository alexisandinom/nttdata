package com.nttdata.accountservice.accounts.application.dto;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountType;
import com.nttdata.accountservice.accounts.infrastructure.jpa.StatusAccount;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.accountservice.transactions.domain.Money;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRequestDTO{
    @NotNull(message = "Customer identification cannot be null")
    @NotBlank(message = "Customer identification cannot be empty")
    @Length(min = 1, max = 13, message = "Customer identification must be between 1 and 13 characters")
    private String identification;

    @NotNull(message = "Account type cannot be null")
    private AccountType type;

    @NotNull(message = "Account number cannot be null")
    @NotBlank(message = "Account number cannot be empty")
    @Length(min = 1, max = 15, message = "Account number must be between 1 and 15 characters")
    private String number;

    @NotNull(message = "Initial balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;

    @NotNull(message = "Account status cannot be null")
    private StatusAccount statusAccount = StatusAccount.ACTIVE;

    public Account toAccount() throws InvalidArgumentException {
        // CustomerReference will be set by AccountManager
        return Account.create(null, null, this.type, this.number, new Money(this.initialBalance), new Money(this.initialBalance), this.statusAccount);
    }
}


