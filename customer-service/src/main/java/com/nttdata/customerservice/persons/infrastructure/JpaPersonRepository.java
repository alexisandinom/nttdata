package com.nttdata.customerservice.persons.infrastructure;

import com.nttdata.core.exceptions.EntityNotFoundException;
import com.nttdata.customerservice.persons.domain.Person;
import com.nttdata.customerservice.persons.domain.PersonRepository;
import com.nttdata.customerservice.persons.infrastructure.jpa.PersonEntity;
import com.nttdata.customerservice.persons.infrastructure.jpa.PersonRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaPersonRepository implements PersonRepository {

    private final PersonRepo personRepo;

    public JpaPersonRepository(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public Person getByIdentification(String identification) {
        PersonEntity entity = personRepo.findByIdentification(identification);
        if (entity == null) {
            throw new EntityNotFoundException("Person with identification " + identification + " not found");
        }
        return entity.toDomain();
    }

    @Override
    public Person save(Person person) {
        PersonEntity entity = personRepo.findByIdentification(person.getIdentification());
        if (entity != null) {
            throw new EntityNotFoundException("Person with identification " + person.getIdentification() + " already exists");
        }
        PersonEntity personEntity = new PersonEntity();
        personEntity.toEntity(person);
        return personRepo.save(personEntity).toDomain();
    }

    @Override
    public Person update(Person person) {
        Optional<PersonEntity> personEntity = Optional.ofNullable(personRepo.findById(person.getId().getValue()))
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
        if (personEntity.isPresent()) {
            personEntity.get().toEntity(person);
        }
        return personRepo.save(personEntity.get()).toDomain();
    }

    @Override
    public void delete(String identification) {
        PersonEntity personEntity = personRepo.findByIdentification(identification);
        if (personEntity != null) {
            personRepo.delete(personEntity);
        } else {
            throw new EntityNotFoundException("Person not found");
        }

    }
}


