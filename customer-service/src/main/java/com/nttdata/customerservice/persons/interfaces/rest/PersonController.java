package com.nttdata.customerservice.persons.interfaces.rest;

import com.nttdata.customerservice.persons.application.PersonManager;
import com.nttdata.customerservice.persons.application.dto.PersonRequestDTO;
import com.nttdata.customerservice.persons.application.dto.PersonResponseDTO;
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
@RequestMapping("/api/v1/persons")
@Tag(name = "Persons", description = "Person management API")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonManager personManager;

    public PersonController(PersonManager personManager) {
        this.personManager = personManager;
    }

    @GetMapping("/{identification}")
    @Operation(summary = "Get person by identification")
    public Mono<ResponseEntity<PersonResponseDTO>> getPerson(@PathVariable String identification) {
        logger.info("Getting person with identification: {}", identification);
        return personManager.getPerson(identification)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error getting person: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    @PostMapping
    @Operation(summary = "Create a new person")
    public Mono<ResponseEntity<PersonResponseDTO>> createPerson(
            @RequestBody @Valid PersonRequestDTO request) {
        logger.info("Creating person with identification: {}", request.getIdentification());
        return personManager.storePerson(request)
                .map(person -> ResponseEntity.status(HttpStatus.CREATED).body(person))
                .onErrorResume(e -> {
                    logger.error("Error creating person: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body((PersonResponseDTO) null));
                });
    }

    @PutMapping("/{identification}")
    @Operation(summary = "Update person")
    public Mono<ResponseEntity<PersonResponseDTO>> updatePerson(
            @PathVariable String identification,
            @RequestBody @Valid PersonRequestDTO request) {
        logger.info("Updating person with identification: {}", identification);
        return personManager.updatePerson(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error updating person: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body((PersonResponseDTO) null));
                });
    }

    @DeleteMapping("/{identification}")
    @Operation(summary = "Delete person")
    public Mono<ResponseEntity<Void>> deletePerson(@PathVariable String identification) {
        logger.info("Deleting person with identification: {}", identification);
        return personManager.deletePerson(identification)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    logger.error("Error deleting person: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }
}
