package com.nttdata.accountservice.config;

import com.nttdata.core.exceptions.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CustomerService maintains a local cache of customer information
 * based on Kafka events for eventual consistency.
 * If customer is not in cache, it makes a REST call to customer-service as fallback.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final Map<String, CachedCustomer> customerCache = new ConcurrentHashMap<>();
    private final WebClient webClient;
    private final String customerServiceUrl;

    public CustomerService(@Value("${customer.service.url:http://localhost:8080}") String customerServiceUrl) {
        this.customerServiceUrl = customerServiceUrl;
        this.webClient = WebClient.builder()
                .baseUrl(customerServiceUrl)
                .build();
    }

    public Mono<com.nttdata.accountservice.accounts.domain.CustomerReference> getCustomerByIdentification(String identification) {
        CachedCustomer cached = customerCache.get(identification);
        if (cached != null && cached.isActive()) {
            logger.debug("Found customer in cache: {}", identification);
            return Mono.just(createCustomerFromCache(cached));
        }
        
        // If not in cache, make a REST call to customer-service as fallback
        logger.info("Customer not found in cache: {}. Fetching from customer-service...", identification);
        return fetchCustomerFromService(identification)
                .doOnNext(customer -> {
                    // Update cache with the fetched customer
                    updateCustomerCache(customer.identification(), customer.name(), customer.state());
                    logger.info("Customer fetched from service and cached: {}", identification);
                })
                .map(customer -> new com.nttdata.accountservice.accounts.domain.CustomerReference(
                        customer.identification(),
                        customer.identification(),
                        customer.name(),
                        customer.state()
                ));
    }

    private Mono<CustomerResponse> fetchCustomerFromService(String identification) {
        return webClient.get()
                .uri("/api/v1/customers/{identification}", identification)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    int statusCode = ex.getStatusCode().value();
                    logger.warn("Customer service returned status {} for identification: {}. Response body: {}", 
                            statusCode, identification, ex.getResponseBodyAsString());
                    
                    if (statusCode == HttpStatus.NOT_FOUND.value()) {
                        logger.warn("Customer not found in customer-service: {}", identification);
                        return Mono.error(new InvalidArgumentException("customerId",
                                "Customer with identification " + identification + " not found. Please create the customer first."));
                    }
                    
                    // For other HTTP errors, log details and return appropriate error
                    logger.error("Error fetching customer from customer-service. Status: {}, Message: {}, Response: {}", 
                            statusCode, ex.getMessage(), ex.getResponseBodyAsString());
                    return Mono.error(new InvalidArgumentException("customerId",
                            "Customer with identification " + identification + " not found. Please create the customer first."));
                })
                .onErrorResume(Exception.class, ex -> {
                    logger.error("Unexpected error fetching customer from service: {}", ex.getMessage(), ex);
                    return Mono.error(new InvalidArgumentException("customerId",
                            "Customer with identification " + identification + " not found. Please create the customer first."));
                });
    }

    public void updateCustomerCache(String identification, String name, boolean active) {
        customerCache.put(identification, new CachedCustomer(identification, name, active));
        logger.info("Updated customer cache: identification={}, name={}, active={}", 
                identification, name, active);
    }

    public void removeCustomerFromCache(String identification) {
        customerCache.remove(identification);
        logger.info("Removed customer from cache: {}", identification);
    }

    private com.nttdata.accountservice.accounts.domain.CustomerReference createCustomerFromCache(CachedCustomer cached) {
        return new com.nttdata.accountservice.accounts.domain.CustomerReference(
                cached.identification, // Using identification as customerId for now
                cached.identification,
                cached.name,
                cached.active
        );
    }

    static class CachedCustomer {
        final String identification;
        final String name;
        final boolean active;

        CachedCustomer(String identification, String name, boolean active) {
            this.identification = identification;
            this.name = name;
            this.active = active;
        }

        boolean isActive() {
            return active;
        }
    }

    // DTO for Customer Service response
    private record CustomerResponse(
            String id,
            String identification,
            String name,
            Boolean state
    ) {}
}



