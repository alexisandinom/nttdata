package com.nttdata.accountservice.transactions.application.dto;

import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.accountservice.transactions.domain.Money;
import com.nttdata.accountservice.transactions.domain.Transaction;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionType;
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
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TransactionRequestDTO {
    @NotNull(message = "Account number cannot be null")
    @NotBlank(message = "Account number cannot be empty")
    @Length(min = 1, max = 15, message = "Account number must be between 1 and 15 characters")
    private String account;

    @NotNull(message = "Movement type cannot be null")
    private TransactionType type;


    @NotNull(message = "Value cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Value cannot be negative")
    private BigDecimal value;


    public Transaction toTransaction() throws InvalidArgumentException {
        return Transaction.create(null, null, null, this.type,null, new Money(this.value), null);
    }
}


