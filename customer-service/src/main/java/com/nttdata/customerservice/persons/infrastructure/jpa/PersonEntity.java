package com.nttdata.customerservice.persons.infrastructure.jpa;

import com.nttdata.core.infraestructure.jpa.AbstractEntity;
import com.nttdata.customerservice.customers.infrastructure.jpa.CustomerEntity;
import com.nttdata.customerservice.persons.domain.Person;
import com.nttdata.customerservice.persons.domain.PersonId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "persons", uniqueConstraints = {
        @UniqueConstraint(name = "UK_IDENTIFICATION_NUMBER", columnNames = {"identification"})
})
public class PersonEntity extends AbstractEntity {

    @Column(name = "name", length = 75, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 16, nullable = false)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "identification", length = 13, nullable = false)
    private String identification;

    @Column(name = "address", length = 80, nullable = false)
    private String address;

    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @OneToOne(mappedBy = "person")
    private CustomerEntity customer;

    public PersonEntity toEntity(Person person){
        this.setId(Objects.nonNull(person.getId()) ? person.getId().getValue() : null);
        this.name=person.getName();
        this.gender=person.getGender();
        this.age=person.getAge();
        this.identification=person.getIdentification();
        this.address=person.getAddress();
        this.phone=person.getPhone();
        return this;
    }

    public Person toDomain(){
        return Person.create(new PersonId(this.getId()), this.getName(), this.getGender(), this.getAge(),
                this.getIdentification(), this.getAddress(), this.getPhone());
    }
}


