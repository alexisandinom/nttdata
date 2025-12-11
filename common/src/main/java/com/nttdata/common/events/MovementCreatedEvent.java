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
public class MovementCreatedEvent {
    @JsonProperty("movementId")
    private String movementId;
    
    @JsonProperty("accountNumber")
    private String accountNumber;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("value")
    private BigDecimal value;
    
    @JsonProperty("balance")
    private BigDecimal balance;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}

