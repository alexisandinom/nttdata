package com.nttdata.common.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent {
    @JsonProperty("accountId")
    private String accountId;
    
    @JsonProperty("accountNumber")
    private String accountNumber;
    
    @JsonProperty("customerIdentification")
    private String customerIdentification;
    
    @JsonProperty("initialBalance")
    private BigDecimal initialBalance;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}

