package com.nttdata.accountservice.transactions.domain;

import com.nttdata.core.exceptions.DomainException;
import com.nttdata.core.exceptions.InvalidArgumentException;
import com.nttdata.accountservice.transactions.exceptions.MoneyException;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

import static com.nttdata.utils.Validations.validateNonNull;


@Value
public class Money {
    public static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");
    Currency currency;
    BigDecimal amount;

    public Money(Currency currency, BigDecimal amount) throws InvalidArgumentException {
        this.currency = validateNonNull(currency, "currency");
        this.amount = validateNonNull(amount, "amount");
    }

    public Money(BigDecimal amount) throws InvalidArgumentException {
        this(DEFAULT_CURRENCY, amount);
    }

    public Money(String amount) throws InvalidArgumentException {
        this(DEFAULT_CURRENCY, new BigDecimal(amount));
    }

    public Money add(Money money) throws DomainException {
        validateNonNull(money, "money");
        if (!money.currency.equals(currency)) {
            throw new DomainException("cannot add money with different currencies");
        }
        return new Money(currency, amount.add(money.amount));
    }

    public Money subtract(Money money) throws DomainException {
        validateNonNull(money, "money");
        if (!money.currency.equals(currency)) {
            throw new MoneyException("cannot subtract money with different currencies");
        }
        return new Money(currency, amount.subtract(money.amount));
    }

    public Boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Check if this Money is less than another Money
     *
     * @param other the Money with which you want to compare yourself
     * @return true if this Money is less than the other; false otherwise
     * @throws MoneyException if the coins do not match
     */
    public Boolean isLessThan(Money other) throws DomainException {
        validateNonNull(other, "money");
        if (!this.currency.equals(other.currency)) {
            throw new MoneyException("cannot compare money with different currencies");
        }
        return this.amount.compareTo(other.amount) < 0;
    }

    /**
     * Check if this Money is greater than another Money.
     *
     * @param other the Money with which you want to compare yourself
     * @return true if this Money is greater than the other; false otherwise
     * @throws MoneyException if the coins do not match or if the other Money is void
     */
    public Boolean isGreaterThan(Money other) throws DomainException {
        validateNonNull(other, "money");
        if (!this.currency.equals(other.currency)) {
            throw new MoneyException("cannot compare money with different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

}


