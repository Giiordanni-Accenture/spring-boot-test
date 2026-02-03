package com.giordanni.service;

import com.giordanni.exception.ResourceNotFoundException;
import com.giordanni.model.Person;
import com.giordanni.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Person createPerson(Person person){
        Optional<Person> existingPerson = personRepository.findByEmail(person.getEmail());

        if(existingPerson.isPresent()) {
            throw new IllegalArgumentException("Person with email " + person.getEmail() + " already exists.");
        }

        return personRepository.save(person);
    }

    public List<Person> findAllPerson(){
        return personRepository.findAll();
    }

    public Person findById(Long id){
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found."));
    }

    public Person updatePerson(Person person){
        Optional<Person> existingPerson = personRepository.findById(person.getId());

        if(existingPerson.isEmpty()){
            throw new ResourceNotFoundException("Person with id " + person.getId() + " does not exist.");
        }

        return personRepository.save(person);
    }

    public void deletePerson(Long id){
        personRepository.deleteById(id);
    }

}
