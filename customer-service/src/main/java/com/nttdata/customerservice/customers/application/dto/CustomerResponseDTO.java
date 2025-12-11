package com.nttdata.customerservice.customers.application.dto;

import com.nttdata.customerservice.customers.domain.Customer;
import com.nttdata.customerservice.persons.application.dto.PersonResponseDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerResponseDTO extends PersonResponseDTO {
    private String id;
    private String password;
    private Boolean state;

    public CustomerResponseDTO(Customer customer) {
        super(customer.getPerson().getId().getValue().toString(), customer.getPerson().getName(), customer.getPerson().getGender(),
                customer.getPerson().getAge(), customer.getPerson().getIdentification(),customer.getPerson().getAddress(),
                customer.getPerson().getPhone());
        this.id = customer.getId().getValue().toString();
        this.password = customer.getPassword();
        this.state = customer.getState();
    }
}


