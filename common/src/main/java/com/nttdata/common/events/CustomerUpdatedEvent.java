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
public class CustomerUpdatedEvent {
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("identification")
    private String identification;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("state")
    private Boolean state;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}

