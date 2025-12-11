package com.nttdata.customerservice.customers.domain;

import com.nttdata.customerservice.persons.domain.Person;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {
    private CustomerId id;
    private Person person;
    private String password;
    private Boolean state;

    public static Customer create(final CustomerId id, final Person person, final String password, final Boolean state) {
        validatePerson(person);
        validatePassword(password);
        validateState(state);

        return new Customer(id, person, password, state);
    }

    public Customer update(final Person person, final String password, final Boolean state) {
        validatePerson(person);
        validatePassword(password);
        validateState(state);

        this.person = person;
        this.password = password;
        this.state = state;
        return this;
    }

    public Customer changePassword(final String password) {
        validatePassword(password);
        this.password = password;
        return this;
    }

    public Customer changeState(final boolean state) {
        this.state = state;
        return this;
    }

    private static void validatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    private static void validateState(Boolean state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
    }
}


