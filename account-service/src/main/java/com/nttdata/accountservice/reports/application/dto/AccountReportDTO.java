package com.nttdata.accountservice.reports.application.dto;

import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountType;
import com.nttdata.accountservice.accounts.infrastructure.jpa.StatusAccount;
import com.nttdata.accountservice.transactions.application.dto.TransactionResponseDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountReportDTO {
    private AccountType type;
    private String number;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private StatusAccount statusAccount;
    private List<TransactionResponseDTO> movements;

    public AccountReportDTO(String number, AccountType type, BigDecimal initialBalance, 
                           BigDecimal currentBalance, StatusAccount statusAccount, 
                           List<TransactionResponseDTO> movements) {
        this.number = number;
        this.type = type;
        this.initialBalance = initialBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.currentBalance = currentBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.statusAccount = statusAccount;
        this.movements = movements;
    }
}



