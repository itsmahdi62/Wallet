package com.example.wallet.api.repository;

import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")  // Ensure that H2 is used for testing
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testSavePerson() {
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
                .age(33)
                .build();

        // When
        Person savedPerson = personRepository.save(person);

        // Then
        assertNotNull(savedPerson.getId(), "Saved person should have an ID");
        assertEquals("1234567890", savedPerson.getNationalId());
    }

    @Test
    void testFindByNationalId() {
        // Given
        Person person = Person.builder()
                .nationalId("1234567890")
                .name("Jane")
                .family("Doe")
                .phoneNumber("09123456789")
                .dayOfBirth(5)
                .monthOfBirth(10)
                .yearOfBirth(1985)
                .isMale(false)
                .email("jane.doe@example.com")
                .age(38)
                .build();
        personRepository.save(person);

        // When
        Person foundPerson = personRepository.findByNationalId("1234567890");

        // Then
        assertNotNull(foundPerson, "Person should be found by national ID");
        assertEquals("Jane", foundPerson.getName());
    }

    @Test
    void testFindAllExistingPeople() {
        // Given
        Person person1 = Person.builder()
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
        Person person2 = Person.builder()
                .nationalId("0987654321")
                .name("Jane")
                .family("Doe")
                .phoneNumber("09234567890")
                .dayOfBirth(5)
                .monthOfBirth(10)
                .yearOfBirth(1985)
                .isMale(false)
                .email("jane.doe@example.com")
                .age(38)
                .build();
        personRepository.save(person1);
        personRepository.save(person2);

        // When
        List<Person> people = personRepository.listOfExistingPeople();

        // Then
        assertEquals(2, people.size(), "There should be 2 existing people");
    }

    @Test
    void testDeletePerson() {
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
                .age(33)
                .build();
        person = personRepository.save(person);

        // When
        person.setDeletedDate("2024-10-10");
        personRepository.save(person);

        // Then
        Optional<Person> foundPerson = personRepository.findById(person.getId());
        assertTrue(foundPerson.isPresent(), "Person should still exist in the database");
        assertNotNull(foundPerson.get().getDeletedDate(), "Person should have a deleted date");
    }
}