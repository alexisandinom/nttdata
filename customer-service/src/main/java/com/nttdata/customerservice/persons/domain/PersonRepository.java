package com.nttdata.customerservice.persons.domain;

public interface PersonRepository {
    Person getByIdentification (String identification);
    Person save (Person person);
    Person update (Person person);
    void delete (String identification);
}


