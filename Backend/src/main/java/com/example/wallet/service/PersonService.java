package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    // in spring boot best practice is to use inject by constructor instead of autowired
    private final PersonRepository personRepository;

    // For creating Account
    private final AccountService accountService ;

    public Person savePerson(Person person) {
        Person savedPerson = personRepository.save(person);
        // Refactor the code and send this part to Account service
        // This line make a default account for each person
        Account account = accountService.createAccount(savedPerson);
        savedPerson.setAccount(account);
        Person lastPerson = personRepository.save(savedPerson);
        return lastPerson;
    }

    public List<Person> findAllPeople() {
        return personRepository.listOfExistingPeople();
    }

    public Person updatePersonInfo(Long id, Person person) {
        Person updatePerson = personRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Person not found ! "));
        // only these two option can be changed
        updatePerson.setEmail(person.getEmail());
        updatePerson.setPhoneNumber(person.getPhoneNumber());
        return personRepository.save(updatePerson);
    }

    public  void deleteUser(Long id) {
        Person deletePerson = personRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Person not found ! "));
        deletePerson.setDeletedDate(String.valueOf(LocalDate.now()));
        personRepository.save(deletePerson);
    }

    public Person findByNationalId(String nationalId) {
        Person person = personRepository.findByNationalId(nationalId);
        if (person == null) {
            throw new EntityNotFoundException("Person not found with nationalId: " + nationalId);
        }
        return person;
    }

    public Person findOneById(Long id) {
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Person not found ! "));
        return foundPerson;
    }
}