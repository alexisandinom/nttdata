package com.nttdata.customerservice.customers.infrastructure.jpa;

import com.nttdata.core.infraestructure.jpa.AbstractEntity;
import com.nttdata.customerservice.customers.domain.Customer;
import com.nttdata.customerservice.customers.domain.CustomerId;
import com.nttdata.customerservice.persons.infrastructure.jpa.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "customers")
@Getter @Setter @NoArgsConstructor
public class CustomerEntity extends AbstractEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private PersonEntity person;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "state", nullable = false)
    private Boolean state = true;

    public CustomerEntity toEntity(Customer customer){
        this.setId(Objects.nonNull(customer.getId()) ? customer.getId().getValue() : null);
        this.person.setName(customer.getPerson().getName());
        this.person.setGender(customer.getPerson().getGender());
        this.person.setAge(customer.getPerson().getAge());
        this.person.setIdentification(customer.getPerson().getIdentification());
        this.person.setAddress(customer.getPerson().getAddress());
        this.person.setPhone(customer.getPerson().getPhone());
        this.password = customer.getPassword();
        this.state = customer.getState();
        return this;
    }

    public Customer toDomain(){
        return Customer.create(new CustomerId(this.getId()), this.person.toDomain(), this.password, this.state);
    }
}

