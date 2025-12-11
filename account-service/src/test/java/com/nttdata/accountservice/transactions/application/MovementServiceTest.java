package com.nttdata.accountservice.transactions.application;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.accountservice.accounts.domain.AccountId;
import com.nttdata.accountservice.accounts.domain.AccountRepository;
import com.nttdata.accountservice.accounts.domain.CustomerReference;
import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountType;
import com.nttdata.accountservice.accounts.infrastructure.jpa.StatusAccount;
import com.nttdata.accountservice.transactions.application.dto.TransactionRequestDTO;
import com.nttdata.accountservice.transactions.domain.Money;
import com.nttdata.accountservice.transactions.domain.Transaction;
import com.nttdata.accountservice.transactions.domain.TransactionId;
import com.nttdata.accountservice.transactions.domain.TransactionRepository;
import com.nttdata.accountservice.transactions.exceptions.InsufficientBalanceException;
import com.nttdata.accountservice.transactions.infrastructure.jpa.TransactionType;
import com.nttdata.core.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private com.nttdata.accountservice.config.KafkaEventPublisher eventPublisher;

    @InjectMocks
    private MovementManager movementManager;

    private Account testAccount;

    @BeforeEach
    void setUp() throws InvalidArgumentException {
        AccountId accountId = new AccountId(UUID.randomUUID());
        Money initialBalance = new Money(new BigDecimal("100.00"));
        CustomerReference customer = new CustomerReference(
                "CUST123",
                "1234567890",
                "John Doe",
                true
        );
        testAccount = Account.create(accountId, customer, AccountType.SAVINGS_ACCOUNT,
                "123456789", initialBalance, initialBalance, StatusAccount.ACTIVE);
    }

    @Test
    void testDebitWithSufficientBalance() throws InvalidArgumentException, InsufficientBalanceException {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setAccount("123456789");
        request.setType(TransactionType.WITHDRAW);
        request.setValue(new BigDecimal("50.00"));

        when(accountRepository.getByNumber("123456789")).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            
            // Simular la lógica del repositorio real: calcular el balance
            BigDecimal currentAccountBalance = testAccount.getCurrentBalance().getAmount();
            BigDecimal transactionValue = t.getValue().getAmount();
            BigDecimal newBalance;
            
            if (t.getType() == TransactionType.DEPOSIT) {
                newBalance = currentAccountBalance.add(transactionValue);
            } else { // WITHDRAW
                newBalance = currentAccountBalance.subtract(transactionValue);
            }
            
            // Crear una nueva transacción con todos los campos necesarios
            return Transaction.create(
                    new TransactionId(UUID.randomUUID()), // ID asignado por el repositorio
                    t.getAccount(),
                    t.getOrdinal() != null ? t.getOrdinal() : 1L, // Ordinal
                    t.getType(),
                    t.getDate() != null ? t.getDate() : LocalDateTime.now(), // Fecha
                    t.getValue(), // Valor
                    new Money(newBalance) // Balance calculado
            );
        });

        // Act
        var result = movementManager.storeMovement(request).block();

        // Assert
        assertNotNull(result);
        assertEquals("123456789", result.getAccount());
        assertEquals(TransactionType.WITHDRAW, result.getType());
        assertEquals(new BigDecimal("50.00"), result.getValue());
        assertEquals(new BigDecimal("50.00"), result.getBalance()); // 100 - 50 = 50
        verify(accountRepository, times(1)).getByNumber("123456789");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(eventPublisher, times(1)).publishMovementCreated(any());
    }

    @Test
    void testDebitWithInsufficientBalance() throws InvalidArgumentException, InsufficientBalanceException {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setAccount("123456789");
        request.setType(TransactionType.WITHDRAW);
        request.setValue(new BigDecimal("150.00"));

        when(accountRepository.getByNumber("123456789")).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class)))
                .thenThrow(new InsufficientBalanceException("123456789", "Insufficient balance"));

        // Act & Assert
        assertThrows(Exception.class, () -> {
            movementManager.storeMovement(request).block();
        });

        verify(accountRepository, times(1)).getByNumber("123456789");
        verify(eventPublisher, never()).publishMovementCreated(any());
    }
}
