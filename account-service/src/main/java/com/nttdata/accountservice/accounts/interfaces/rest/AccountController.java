package com.nttdata.accountservice.accounts.interfaces.rest;

import com.nttdata.accountservice.accounts.application.AccountManager;
import com.nttdata.accountservice.accounts.application.dto.AccountRequestDTO;
import com.nttdata.accountservice.accounts.application.dto.AccountResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "Account management API")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountManager accountManager;

    public AccountController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @GetMapping("/{number}")
    @Operation(summary = "Get account by number")
    public Mono<ResponseEntity<AccountResponseDTO>> getAccount(@PathVariable String number) {
        logger.info("Getting account with number: {}", number);
        return accountManager.getAccount(number)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error getting account: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public Mono<ResponseEntity<AccountResponseDTO>> createAccount(
            @RequestBody @Valid AccountRequestDTO request) {
        logger.info("Creating account with number: {}", request.getNumber());
        return accountManager.storeAccount(request)
                .map(account -> ResponseEntity.status(HttpStatus.CREATED).body(account))
                .onErrorResume(e -> {
                    logger.error("Error creating account: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body((AccountResponseDTO) null));
                });
    }

    @PutMapping("/{number}")
    @Operation(summary = "Update account status")
    public Mono<ResponseEntity<AccountResponseDTO>> updateAccount(
            @PathVariable String number,
            @RequestBody @Valid AccountRequestDTO request) {
        logger.info("Updating account with number: {}", number);
        return accountManager.updateStatusAccount(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error updating account: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body((AccountResponseDTO) null));
                });
    }

    @DeleteMapping("/{number}")
    @Operation(summary = "Delete account")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable String number) {
        logger.info("Deleting account with number: {}", number);
        return accountManager.deleteAccount(number)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    logger.error("Error deleting account: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }
}


