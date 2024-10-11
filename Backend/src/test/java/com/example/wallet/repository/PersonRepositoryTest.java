package com.example.wallet.repository;

import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import org.assertj.core.api.Assertions;
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
        // Arrange
        Person person = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("ahdiAlmasi@example.com")
                .build();

        // Act
        Person savedPerson = personRepository.save(person);
        // Assert
        Assertions.assertThat(savedPerson).isNotNull();
        Assertions.assertThat(savedPerson.getId()).isGreaterThan(0);
    }

    @Test
    void testFindByNationalId() {
        // Given
        Person person = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("ahdiAlmasi@example.com")
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
        Person person1 = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("ahdiAlmasi@example.com")
                .build();
        Person person2 = Person.builder()
                .nationalId("1987654321")
                .name("Hatam")
                .family("Taii")
                .phoneNumber("09234567890")
                .dayOfBirth(5)
                .monthOfBirth(10)
                .yearOfBirth(1985)
                .isMale(false)
                .email("hatamtaii@example.com")
                .build();
        personRepository.save(person1);
        personRepository.save(person2);

        List<Person> people = personRepository.listOfExistingPeople();

        Assertions.assertThat(people).isNotNull();
        Assertions.assertThat(people.size()).isEqualTo(2);
    }

    @Test
    void testDeletePerson() {
        Person person = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("ahdiAlmasi@example.com")
                .build();
        person = personRepository.save(person);

        person.setDeletedDate("2024-10-10");
        personRepository.save(person);

        Optional<Person> foundPerson = personRepository.findById(person.getId());
        assertTrue(foundPerson.isPresent(), "Person should still exist in the database");
        assertNotNull(foundPerson.get().getDeletedDate(), "Person should have a deleted date");
    }

    @Test
    void testFindByIdPerson() {
        Person person = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("ahdiAlmasi@example.com")
                .build();
        person = personRepository.save(person);

        Optional<Person> foundPerson = personRepository.findById(person.getId());
        assertTrue(foundPerson.isPresent(), "Person exist in the database");
        assertNotNull(foundPerson.get().getName(), "This person");
    }

    @Test
    public void testUpdatePerson() {
        Person person = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("ahdiAlmasi@example.com")
                .build();

        personRepository.save(person);

        Person savedPerson = personRepository.findById(person.getId()).get();
        savedPerson.setName("mahdi");

        Person updatedPokemon = personRepository.save(savedPerson);

        Assertions.assertThat(updatedPokemon.getName()).isNotNull();
    }
}