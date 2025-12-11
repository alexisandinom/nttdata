package com.nttdata.customerservice.customers.domain;

public interface CustomerRepository {
    Customer getByIdentification (String identification);
    Customer save (Customer person);
    Customer update (Customer person);
    void delete (String identification);
}


