package com.nttdata.customerservice.persons.domain;

import com.nttdata.customerservice.persons.infrastructure.jpa.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Person {
    private PersonId id;
    private String name;
    private Gender gender;
    private int age;
    private String identification;
    private String address;
    private String phone;

    public static Person create(final PersonId personId, final String name, final Gender gender, final int age, final String identification,
                                final String address, final String phone) {
        return new Person(personId, name, gender, age, identification, address, phone);
    }

    public Person update(final String name, final Gender gender, final int age,
                                final String address, final String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.phone = phone;
        return this;
    }
}


