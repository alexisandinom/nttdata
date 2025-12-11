package com.nttdata.customerservice.persons.application;

import com.nttdata.customerservice.persons.application.dto.PersonRequestDTO;
import com.nttdata.customerservice.persons.application.dto.PersonResponseDTO;
import com.nttdata.customerservice.persons.domain.Person;
import com.nttdata.customerservice.persons.domain.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PersonManager {

    private static final Logger logger = LoggerFactory.getLogger(PersonManager.class);
    private final PersonRepository personRepository;

    public PersonManager(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Mono<PersonResponseDTO> storePerson(PersonRequestDTO dto) {
        logger.info("Storing person with identification: {}", dto.getIdentification());
        return Mono.fromCallable(() -> {
                    Person person = personRepository.save(dto.toPerson());
                    return person;
                })
                .map(PersonResponseDTO::new);
    }

    public Mono<PersonResponseDTO> updatePerson(PersonRequestDTO dto) {
        logger.info("Updating person with identification: {}", dto.getIdentification());
        return Mono.fromCallable(() -> {
                    Person person = personRepository.getByIdentification(dto.getIdentification());
                    person.update(dto.getName(), dto.getGender(), dto.getAge(), dto.getAddress(), dto.getPhone());
                    return personRepository.update(person);
                })
                .map(PersonResponseDTO::new);
    }

    public Mono<PersonResponseDTO> getPerson(String identification) {
        logger.info("Getting person with identification: {}", identification);
        return Mono.fromCallable(() -> {
                    Person person = personRepository.getByIdentification(identification);
                    return person;
                })
                .map(PersonResponseDTO::new);
    }

    public Mono<Void> deletePerson(String identification) {
        logger.info("Deleting person with identification: {}", identification);
        return Mono.fromRunnable(() -> {
                    personRepository.delete(identification);
                });
    }
}
