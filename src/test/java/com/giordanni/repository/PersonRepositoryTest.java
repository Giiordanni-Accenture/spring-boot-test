package com.giordanni.repository;

import com.giordanni.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // carrega application-test.yml
@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    Person person1;
    Person person2;

    @BeforeEach
    void setUp(){
        person1 = new Person("Giordanni", "Formiga", "123 Main St", "M", "giordanniformiga@gmail.com");
        person2 = new Person("Emily", "Pereira", "456 Elm St", "F", "emily@gmail.com");
    }

    @Test
    void shouldCreatePersonAndGenerateId(){

        Person savedPerson = personRepository.save(person1);

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    void shouldReturnAllPersonsWhenTheyExist(){
        personRepository.save(person1);
        personRepository.save(person2);

        List<Person> personList = personRepository.findAll();

        assertNotNull(personList);
        assertEquals(2, personList.size());
        assertEquals(person1, personList.get(0));
    }

    @Test
    void shouldReturnPersonById(){
        Person personSaved1 = personRepository.save(person1);

        Person personFound = personRepository.findById(personSaved1.getId()).get();

        assertEquals(personFound.getId(), personSaved1.getId());
        assertNotNull(personFound);
    }

    @Test
    void shouldReturnPersonWhenFindByEmail(){
        personRepository.save(person1);

        Optional<Person> personFound = personRepository.findByEmail(person1.getEmail());

        assertNotNull(personFound);
        assertTrue(personFound.isPresent());
        assertEquals(person1, personFound.get());
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound(){
        personRepository.save(person1);

        Optional<Person> personFound = personRepository.findByEmail(person2.getEmail());

        assertFalse(personFound.isPresent());
    }

    @Test
    void shouldUpdatePersonSuccessfully(){
        Person personSaved = personRepository.save(person1);

        personSaved.setFirstName("Giordanni Updated");
        personSaved.setAddress("Updated Address");

        Person personUpdated = personRepository.save(personSaved);
        Person personFound = personRepository.findById(personUpdated.getId()).get();

        assertEquals("Giordanni Updated", personUpdated.getFirstName());
        assertEquals("Updated Address", personUpdated.getAddress());
        assertNotNull(personFound);
    }

    @Test
    void shouldDeletePersonRemovesIt(){
        personRepository.save(person1);

        personRepository.deleteById(person1.getId());

        Optional<Person> optionalPerson = personRepository.findById(person1.getId());

        assertTrue(optionalPerson.isEmpty());
    }

    @Test
    void shouldFindPersonByJPQLWithIndices(){
        personRepository.save(person1);

        Person findPerson = personRepository.findByJPQL(person1.getFirstName(), person1.getLastName());

        assertNotNull(findPerson);
        assertEquals(person1.getFirstName(), findPerson.getFirstName());
        assertEquals(person1.getLastName(), findPerson.getLastName());
    }

    @Test
    void shouldFindPersonByJPQLWithNamedParams(){
        personRepository.save(person1);

        Person findPerson = personRepository.findByJPQLNamedParams(person1.getFirstName(), person1.getLastName());

        assertNotNull(findPerson);
        assertEquals(person1.getFirstName(), findPerson.getFirstName());
        assertEquals(person1.getLastName(), findPerson.getLastName());
    }

    @Test
    void shouldFindPersonByNativeSQL(){
        personRepository.save(person2);

        Person findPerson = personRepository.findByNativeSQL(person2.getFirstName(), person2.getLastName());

        assertNotNull(findPerson);
        assertEquals(person2.getFirstName(), findPerson.getFirstName());
        assertEquals(person2.getLastName(), findPerson.getLastName());
    }
}