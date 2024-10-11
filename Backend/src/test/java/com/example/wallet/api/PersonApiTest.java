package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

//@SpringBootTest
@WebMvcTest(PersonAPI.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")  // Ensure H2 is used for testing
class PersonApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;  // Used for converting objects to JSON

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();  // Clean up the database before each test
    }
    @Test
    void testCreatePerson() throws Exception {
        // Given
        Person person = Person.builder()
                .nationalId("1234567890")
                .name("John")
                .family("Doe")
                .phoneNumber("09123456789")
                .dayOfBirth(1)
                .monthOfBirth(1)
                .yearOfBirth(1990)
                .isMale(true)
                .email("john.doe@example.com")
                .build();

        // When & Then
        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nationalId").value("1234567890"));
    }

    @Test
    void testGetPersonById() throws Exception {
        // Given
        Person person = Person.builder()
                .nationalId("1234567890")
                .name("John")
                .family("Doe")
                .phoneNumber("09123456789")
                .dayOfBirth(1)
                .monthOfBirth(1)
                .yearOfBirth(1990)
                .isMale(true)
                .email("john.doe@example.com")
                .build();
        person = personRepository.save(person);

        // When & Then
        mockMvc.perform(get("/api/persons/" + person.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.nationalId").value("1234567890"));
    }

    @Test
    void testDeletePerson() throws Exception {
        // Given
        Person person = Person.builder()
                .nationalId("1234567890")
                .name("John")
                .family("Doe")
                .phoneNumber("09123456789")
                .dayOfBirth(1)
                .monthOfBirth(1)
                .yearOfBirth(1990)
                .isMale(true)
                .email("john.doe@example.com")
                .build();
        person = personRepository.save(person);

        // When & Then
        mockMvc.perform(delete("/api/persons/" + person.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/persons/" + person.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}