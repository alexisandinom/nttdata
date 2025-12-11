package com.nttdata.customerservice.persons.infrastructure.jpa;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepo extends CrudRepository<PersonEntity, UUID> {
    boolean existsByIdentification(String identification);
    PersonEntity findByIdentification(String identification);
}


