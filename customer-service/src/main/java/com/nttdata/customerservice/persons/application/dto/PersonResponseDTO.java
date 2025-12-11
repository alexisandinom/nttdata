package com.nttdata.customerservice.persons.application.dto;

import com.nttdata.customerservice.persons.domain.Person;
import com.nttdata.customerservice.persons.infrastructure.jpa.Gender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PersonResponseDTO {
    private String id;
    private String name;
    private Gender gender;
    private Integer age;
    private String identification;
    private String address;
    private String phone;

    public PersonResponseDTO(Person person) {
        this.id = person.getId().getValue().toString();
        this.name = person.getName();
        this.gender = person.getGender();
        this.age = person.getAge();
        this.identification = person.getIdentification();
        this.address = person.getAddress();
        this.phone = person.getPhone();
    }

    public PersonResponseDTO(String id, String name, Gender gender, Integer age, String identification, String address, String phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.identification = identification;
        this.address = address;
        this.phone = phone;
    }
}


