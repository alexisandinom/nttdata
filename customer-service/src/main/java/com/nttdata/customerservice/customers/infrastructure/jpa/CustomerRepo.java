package com.nttdata.customerservice.customers.infrastructure.jpa;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepo extends CrudRepository<CustomerEntity, UUID> {
    CustomerEntity findByPerson_Identification(String identification);
}


