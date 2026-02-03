package com.giordanni.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.giordanni.exception.ResourceNotFoundException;
import com.giordanni.model.Person;
import com.giordanni.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonController.class) // se colocar entre parentese informo qual controller quero testar e evito de subir todos
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService service;

    Person person1;
    Person person2;

    @BeforeEach
    void setUp(){
        person1 = new Person("Giordanni", "Formiga", "123 Main St", "M", "giordanniformiga@gmail.com");
        person2 = new Person("Emily", "Pereira", "456 Elm St", "F", "emily@gmail.com");
    }

    @Test
    void shouldCreatePersonControllerTest() throws JsonProcessingException, Exception {

        when(service.createPerson(any(Person.class)))
                .thenAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person1)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person1.getLastName())))
                .andExpect(jsonPath("$.email", is(person1.getEmail())));
    }

    @Test
    void shouldFindAllPersonControllerTest() throws JsonProcessingException, Exception {

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        when(service.findAllPerson())
                .thenReturn(personList);

        ResultActions response = mockMvc.perform(get("/person/all"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(personList.size())));
    }

    @Test
    void shouldFindByIdPersonControllerTest() throws JsonProcessingException, Exception {
        Long personId = 1L;
        when(service.findById(personId))
                .thenReturn(person1);

        ResultActions response = mockMvc.perform(get("/person/{id}",personId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person1.getLastName())))
                .andExpect(jsonPath("$.email", is(person1.getEmail())));
    }

    @Test
    void shouldReturnEmptyWhenPersonNotFoundByIdTest() throws JsonProcessingException, Exception {
        Long personId = 1L;
        when(service.findById(personId))
                .thenThrow(ResourceNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/person/{id}",personId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdatePersonTest() throws JsonProcessingException, Exception {

        Long personId = 1L;
        when(service.findById(personId)).thenReturn(person1);
        when(service.updatePerson(any(Person.class)))
                .thenAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person2)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person2.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person2.getLastName())))
                .andExpect(jsonPath("$.email", is(person2.getEmail())));
    }

    @Test
    void shouldErrorUpdatePersonTest() throws JsonProcessingException, Exception {

        Long personId = 1L;
        when(service.findById(personId)).thenThrow(ResourceNotFoundException.class);
        when(service.updatePerson(any(Person.class)))
                .thenAnswer((invocation) -> invocation.getArgument(1));

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person2)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeletePersonTest() throws Exception {
        Long personId = 1L;

        willDoNothing().given(service).deletePerson(personId);

        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}