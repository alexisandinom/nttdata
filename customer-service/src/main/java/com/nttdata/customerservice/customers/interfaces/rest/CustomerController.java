package com.nttdata.customerservice.customers.interfaces.rest;

import com.nttdata.customerservice.customers.application.CustomerManager;
import com.nttdata.customerservice.customers.application.dto.CustomerRequestDTO;
import com.nttdata.customerservice.customers.application.dto.CustomerResponseDTO;
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
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Customer management API")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerManager customerManager;

    public CustomerController(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    @GetMapping("/{identification}")
    @Operation(summary = "Get customer by identification")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomer(@PathVariable String identification) {
        logger.info("Getting customer with identification: {}", identification);
        return customerManager.getCustomer(identification)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public Mono<ResponseEntity<CustomerResponseDTO>> createCustomer(
            @RequestBody @Valid CustomerRequestDTO request) {
        logger.info("Creating customer with identification: {}", request.getIdentification());
        return customerManager.storeCustomer(request)
                .map(customer -> ResponseEntity.status(HttpStatus.CREATED).body(customer));
    }

    @PutMapping("/{identification}")
    @Operation(summary = "Update customer")
    public Mono<ResponseEntity<CustomerResponseDTO>> updateCustomer(
            @PathVariable String identification,
            @RequestBody @Valid CustomerRequestDTO request) {
        logger.info("Updating customer with identification: {}", identification);
        return customerManager.updateCustomer(request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{identification}")
    @Operation(summary = "Delete customer")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String identification) {
        logger.info("Deleting customer with identification: {}", identification);
        return customerManager.deleteCustomer(identification)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }
}


