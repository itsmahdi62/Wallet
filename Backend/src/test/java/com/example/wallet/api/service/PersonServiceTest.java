package com.example.wallet.api.service;

import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import com.example.wallet.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        // Initialize a Person entity for testing
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
                .age(33)
                .build();

        personService.savePerson(person);
    }

    @Test
    void testFindAllPeople() {
        List<Person> people = personService.findAllPeople();
        assertFalse(people.isEmpty(), "The list of people should not be empty");
    }

    @Test
    void testFindByNationalId() {
        Person person = personService.findByNationalId("1234567890");
        assertNotNull(person, "Person should be found by nationalId");
        assertEquals("John", person.getName(), "Person's name should be John");
    }

    @Test
    void testUpdatePersonInfo() {
        Person person = personService.findByNationalId("1234567890");
        person.setEmail("new.email@example.com");
        personService.updatePersonInfo(person.getId(), person);

        Person updatedPerson = personService.findByNationalId("1234567890");
        assertEquals("new.email@example.com", updatedPerson.getEmail(), "Email should be updated");
    }

    @Test
    void testDeleteUser() {
        Person person = personService.findByNationalId("1234567890");
        personService.deleteUser(person.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            personService.findByNationalId("1234567890");
        });

        String expectedMessage = "Person not found with nationalId: 1234567890";
        assertTrue(exception.getMessage().contains(expectedMessage), "Expected not found exception");
    }
}
