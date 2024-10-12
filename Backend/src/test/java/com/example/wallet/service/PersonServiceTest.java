package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PersonServiceTest {
    @InjectMocks
    private PersonService personService;
    @Mock
    private AccountService accountService;
    @Mock
    private PersonRepository personRepository;
    private Person person;
    @BeforeEach
    void setUp() {
        // Initialize a Person entity for testing
        person = Person.builder()
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
    }
    @Test
    void testCreatePerson() {
        // Mock the save method of personRepository
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // Mock the createAccount method of accountService
        Account mockAccount = new Account();
        mockAccount.setAccountNumber("1234567890123");
        when(accountService.createAccount(any(Person.class))).thenReturn(mockAccount);

        // Call the method to test
        Person savedPerson = personService.savePerson(person);

        // Verify the returned Person object is not null and is correctly set up
        assertNotNull(savedPerson, "Saved person should not be null");
        assertEquals("3920841271", savedPerson.getNationalId());
        assertEquals("Mahdi", savedPerson.getName());
        assertNotNull(savedPerson.getAccount(), "Account should not be null");
        assertEquals("1234567890123", savedPerson.getAccount().getAccountNumber());
    }
    @Test
    void testFindAllPeople() {
        // Create a list of people to return from the mock repository
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

        // Mock the personRepository.listOfExistingPeople() to return a non-empty list
        when(personRepository.listOfExistingPeople()).thenReturn(List.of(person1, person2));

        // Call the service method
        List<Person> people = personService.findAllPeople();

        // Assert that the list is not empty
        assertFalse(people.isEmpty(), "The list of people should not be empty");
        assertEquals(2, people.size(), "The list should contain 2 people");
    }

    @Test
    void testFindByNationalId() {
        // Arrange: Create a mock Person object
        Person mockPerson = Person.builder()
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .dayOfBirth(22)
                .monthOfBirth(5)
                .yearOfBirth(2001)
                .isMale(true)
                .email("amiralmasi@gail.com")
                .build();

        // Mock the behavior of personRepository.findByNationalId() to return mockPerson
        when(personRepository.findByNationalId("3920841271")).thenReturn(mockPerson);

        // Act: Call the method under test
        Person foundPerson = personService.findByNationalId("1234567890");

        // Assert: Check if the person object is correctly returned
        assertNotNull(foundPerson, "The person should not be null");
        assertEquals("1234567890", foundPerson.getNationalId(), "The national ID should match");
        assertEquals("Mahdi", foundPerson.getName(), "The name should match");
        assertEquals("Almasi", foundPerson.getFamily(), "The family name should match");
        assertEquals("09391395538", foundPerson.getPhoneNumber(), "The phone number should match");
        assertEquals(22, foundPerson.getDayOfBirth(), "The day of birth should match");
        assertEquals(5, foundPerson.getMonthOfBirth(), "The month of birth should match");
        assertEquals(2001, foundPerson.getYearOfBirth(), "The year of birth should match");
        assertTrue(foundPerson.getIsMale(), "The gender should match");
        assertEquals("mahdi@example.com", foundPerson.getEmail(), "The email should match");
    }

    @Test
    void testUpdatePersonInfo() {
        // Arrange: Create an existing Person object
        Person existingPerson = Person.builder()
                .id(1L)
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .email("amiralmasi@gmail.com")
                .build();

        // Create an updated Person object
        Person updatedPerson = Person.builder()
                .id(1L)
                .nationalId("3920841271")
                .name("Mahdi")
                .family("Almasi")
                .phoneNumber("09391395538")
                .email("amiralmasi021@gmail.com") // Updated email
                .build();

        // Mock the behavior of personRepository.findById() to return the existing person
        when(personRepository.findById(1L)).thenReturn(java.util.Optional.of(existingPerson));

        // Mock the save method to return the updated person
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        // Act: Call the method under test
        Person resultPerson = personService.updatePersonInfo(1L, updatedPerson);

        // Assert: Verify that the person was updated correctly
        assertNotNull(resultPerson, "The updated person should not be null");
        assertEquals("1234567890", resultPerson.getNationalId(), "The national ID should not change");
        assertEquals("Mahdi", resultPerson.getName(), "The name should remain unchanged");
        assertEquals("Almasi", resultPerson.getFamily(), "The family name should remain unchanged");
        assertEquals("09391395538", resultPerson.getPhoneNumber(), "The phone number should remain unchanged");
        assertEquals("new.email@example.com", resultPerson.getEmail(), "The email should be updated");
    }

    // because wh use logical delete , it is like testUpdate we do not need testDelete

}
