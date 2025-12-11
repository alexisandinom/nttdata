package com.nttdata.accountservice.reports.application.dto;

import com.nttdata.accountservice.accounts.domain.Account;
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
public class ReportResponseDTO {
    private String identification;
    private String customerName;
    private List<AccountReportDTO> accounts;

    public ReportResponseDTO(String identification, String customerName, List<AccountReportDTO> accounts) {
        this.identification = identification;
        this.customerName = customerName;
        this.accounts = accounts;
    }
}


