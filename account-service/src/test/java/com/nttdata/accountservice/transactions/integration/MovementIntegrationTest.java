package com.nttdata.accountservice.transactions.integration;

import com.nttdata.accountservice.AccountServiceApplication;
import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountId;
import com.nttdata.accountservice.accounts.domain.AccountRepository;
import com.nttdata.accountservice.accounts.domain.CustomerReference;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountType;
import com.nttdata.accountservice.accounts.infrastructure.jpa.StatusAccount;
import com.nttdata.accountservice.transactions.application.MovementManager;
import com.nttdata.accountservice.transactions.application.dto.TransactionRequestDTO;
import com.nttdata.accountservice.transactions.domain.Money;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionType;
import com.nttdata.core.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AccountServiceApplication.class)
@ActiveProfiles("test")
@Transactional
class MovementIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementManager movementManager;

    @Test
    void testCreateAccountAndMovement() throws InvalidArgumentException {
        // Create Account
        AccountId accountId = new AccountId(UUID.randomUUID());
        Money initialBalance = new Money(new BigDecimal("200.00"));
        CustomerReference customer = new CustomerReference(
                "CUST123",
                "TEST123456",
                "Test User",
                true
        );
        Account account = Account.create(accountId, customer, AccountType.SAVINGS_ACCOUNT, 
                "ACC123456", initialBalance, initialBalance, StatusAccount.ACTIVE);
        Account savedAccount = accountRepository.save(account);
        
        // Create Movement (Deposit)
        TransactionRequestDTO movementRequest = new TransactionRequestDTO();
        movementRequest.setAccount("ACC123456");
        movementRequest.setType(TransactionType.DEPOSIT);
        movementRequest.setValue(new BigDecimal("50.00"));
        
        var movementResult = movementManager.storeMovement(movementRequest).block();
        
        // Assertions
        assertNotNull(movementResult);
        assertEquals("ACC123456", movementResult.getAccount());
        assertEquals(TransactionType.DEPOSIT, movementResult.getType());
        assertEquals(new BigDecimal("50.00"), movementResult.getValue());
        
        // Verify account balance was updated
        Account updatedAccount = accountRepository.getByNumber("ACC123456");
        assertEquals(new BigDecimal("250.00"), updatedAccount.getCurrentBalance().getAmount()); // 200 + 50 = 250
    }
}

