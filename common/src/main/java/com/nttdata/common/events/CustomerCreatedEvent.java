package com.nttdata.common.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreatedEvent {
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("personId")
    private String personId;
    
    @JsonProperty("identification")
    private String identification;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}

