package com.nttdata.accountservice.config;

import com.nttdata.accountservice.accounts.domain.Account;
import com.nttdata.core.exceptions.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CustomerService maintains a local cache of customer information
 * based on Kafka events for eventual consistency.
 * In a production environment, this could also make REST calls to customer-service.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final Map<String, CachedCustomer> customerCache = new ConcurrentHashMap<>();

    public Mono<com.nttdata.accountservice.accounts.domain.CustomerReference> getCustomerByIdentification(String identification) {
        CachedCustomer cached = customerCache.get(identification);
        if (cached != null && cached.isActive()) {
            logger.debug("Found customer in cache: {}", identification);
            return Mono.just(createCustomerFromCache(cached));
        }
        
        // If not in cache, try to get from cache again (eventual consistency)
        // In production, you might want to make a REST call to customer-service here
        
        // In a real scenario, you might make a REST call to customer-service here
        // For now, we'll throw an exception if customer is not in cache
        logger.warn("Customer not found in cache: {}", identification);
        return Mono.error(new InvalidArgumentException("customerId", 
                "Customer not found. Please ensure customer is created first."));
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
}



