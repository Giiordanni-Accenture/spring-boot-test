package com.giordanni.service;

import com.giordanni.exception.ResourceNotFoundException;
import com.giordanni.model.Person;
import com.giordanni.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService services;

    @Mock
    private PersonRepository repository;

    Person person1;
    Person person2;

    @BeforeEach
    void setUp(){
        person1 = new Person("Giordanni", "Formiga", "123 Main St", "M", "giordanniformiga@gmail.com");
        person2 = new Person("Emily", "Pereira", "456 Elm St", "F", "emily@gmail.com");
    }

    @Test
    void shouldCreatePersonTest(){

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Person.class))).thenReturn(person1);

        Person createdPerson = services.createPerson(person1);

        verify(repository).save(any(Person.class));
        verify(repository).findByEmail(anyString());
        assertNotNull(createdPerson);
    }

    @Test
    void shouldNotCreatePersonWhenEmailExistsTest(){
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person1));

        IllegalArgumentException error =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> services.createPerson(person1));

        verify(repository, never()).save(any(Person.class));
        assertEquals("Person with email " + person1.getEmail() + " already exists.", error.getMessage());
    }

    @Test
    void shouldFindAllPersonTest(){
        given(repository.findAll()).willReturn(List.of(person1, person2));

        List<Person> persons = services.findAllPerson();

        verify(repository).findAll();
        assertEquals(2, persons.size());
    }

    @Test
    void shouldErrorWhenNoPersonFoundTest(){
        given(repository.findAll()).willReturn(List.of());

        List<Person> persons = services.findAllPerson();

        verify(repository).findAll();
        assertTrue(persons.isEmpty());
    }

    @Test
    void shouldFindPersonByIdTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(person1));

        Person foundPerson = services.findById(1L);

        verify(repository).findById(anyLong());
        assertNotNull(foundPerson);
        assertEquals(person1.getEmail(), foundPerson.getEmail());
    }

    @Test
    void shouldReturnEmptyWhenPersonNotFoundByIdTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var error = assertThrows(
                ResourceNotFoundException.class,
                () -> services.findById(1L)
        );

        verify(repository).findById(anyLong());
        assertEquals("Person with id 1 not found.", error.getMessage());
    }

    @Test
    void shouldUpdatePerson(){
        person1.setId(1L);
        when(repository.findById(anyLong())).thenReturn(Optional.of(person1));

        person1.setEmail("giordannialves@gmail.com");
        person1.setLastName("Alves");

        when(repository.save(person1)).thenReturn(person1);

        Person updatedPerson = services.updatePerson(person1);

        verify(repository, timeout(1)).findById(anyLong());
        verify(repository).save(any(Person.class));
        assertNotNull(updatedPerson);
        assertEquals("giordannialves@gmail.com", updatedPerson.getEmail());
    }

    @Test
    void shouldNotUpdateWhenPersonDoesNotExistTest(){
        person1.setId(1L);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException error =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> services.updatePerson(person1));

        verify(repository, never()).save(any(Person.class));
        assertEquals("Person with id " + person1.getId() + " does not exist.", error.getMessage());
    }

    @Test
    void shouldDeletePersonTest(){
        person1.setId(1L);
        doNothing().when(repository).deleteById(anyLong());
        services.deletePerson(person1.getId());

        verify(repository, times(1)).deleteById(anyLong());
    }

}