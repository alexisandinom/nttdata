package com.nttdata.accountservice.transactions.interfaces.rest;

import com.nttdata.accountservice.transactions.application.MovementManager;
import com.nttdata.accountservice.transactions.application.dto.TransactionRequestDTO;
import com.nttdata.accountservice.transactions.application.dto.TransactionResponseDTO;
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
@RequestMapping("/api/v1/movements")
@Tag(name = "Movements", description = "Movement (Transaction) management API")
public class MovementController {

    private static final Logger logger = LoggerFactory.getLogger(MovementController.class);
    private final MovementManager movementManager;

    public MovementController(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movement by ID")
    public Mono<ResponseEntity<TransactionResponseDTO>> getMovement(@PathVariable String id) {
        logger.info("Getting movement with id: {}", id);
        return movementManager.getMovement(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error getting movement: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    @PostMapping
    @Operation(summary = "Create a new movement")
    public Mono<ResponseEntity<TransactionResponseDTO>> createMovement(
            @RequestBody @Valid TransactionRequestDTO request) {
        logger.info("Creating movement for account: {}, type: {}, value: {}", 
                request.getAccount(), request.getType(), request.getValue());
        return movementManager.storeMovement(request)
                .map(movement -> ResponseEntity.status(HttpStatus.CREATED).body(movement))
                .onErrorResume(e -> {
                    logger.error("Error creating movement: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body((TransactionResponseDTO) null));
                });
    }
}



