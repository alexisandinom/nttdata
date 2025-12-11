package com.nttdata.customerservice.customers.infrastructure;

import com.nttdata.core.exceptions.EntityNotFoundException;
import com.nttdata.customerservice.customers.domain.Customer;
import com.nttdata.customerservice.customers.domain.CustomerRepository;
import com.nttdata.customerservice.customers.infrastructure.jpa.CustomerEntity;
import com.nttdata.customerservice.customers.infrastructure.jpa.CustomerRepo;
import com.nttdata.customerservice.persons.infrastructure.jpa.PersonEntity;
import com.nttdata.customerservice.persons.infrastructure.jpa.PersonRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaCustomerRepository implements CustomerRepository {

    private final CustomerRepo customerRepo;
    private final PersonRepo personRepo;

    public JpaCustomerRepository(CustomerRepo customerRepo, PersonRepo personRepo) {
        this.customerRepo = customerRepo;
        this.personRepo = personRepo;
    }

    @Override
    public Customer getByIdentification(String identification) {
        CustomerEntity entity = customerRepo.findByPerson_Identification(identification);
        if (entity == null) {
            throw new EntityNotFoundException("Customer with identification " + identification + " not found");
        }
        return entity.toDomain();
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = customerRepo.findByPerson_Identification(customer.getPerson().getIdentification());
        if (entity != null) {
            throw new EntityNotFoundException("Customer with identification " + customer.getPerson().getIdentification() + " already exists");
        }

        PersonEntity personEntity = new PersonEntity().toEntity(customer.getPerson());
        PersonEntity savedPerson = personRepo.save(personEntity);


        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPerson(savedPerson);
        customerEntity.setPassword(customer.getPassword());
        customerEntity.setState(customer.getState());

        return customerRepo.save(customerEntity).toDomain();
    }

    @Override
    public Customer update(Customer customer) {
        Optional<CustomerEntity> customerEntity = Optional.ofNullable(customerRepo.findById(customer.getId().getValue()))
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        if (customerEntity.isPresent()) {
            customerEntity.get().toEntity(customer);
        }
        return customerRepo.save(customerEntity.get()).toDomain();
    }

    @Override
    @Transactional
    public void delete(String identification) {
        CustomerEntity customerEntity = customerRepo.findByPerson_Identification(identification);
        if (customerEntity != null) {
            // In a microservices architecture, we can't check accounts here
            // The account-service should handle account deletion validation
            customerRepo.delete(customerEntity);
            personRepo.delete(customerEntity.getPerson());
        } else {
            throw new EntityNotFoundException("Customer not found");
        }

    }
}


