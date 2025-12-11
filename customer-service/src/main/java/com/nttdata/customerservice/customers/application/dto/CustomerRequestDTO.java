package com.nttdata.customerservice.customers.application.dto;

import com.nttdata.customerservice.customers.domain.Customer;
import com.nttdata.customerservice.persons.application.dto.PersonRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequestDTO extends PersonRequestDTO {

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    @Length(min = 1, max = 20, message = "Password must be between 1 and 20 characters")
    private String password;

    @NotNull(message = "State cannot be null")
    private Boolean state = true;


    public Customer toCustomer() {
        return Customer.create(null, this.toPerson(),this.password, this.state);
    }
}


