package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    // in spring boot best practice is to use inject by constructor instead of autowired
    private final PersonRepository personRepository;


    public Person savePerson(Person person) {
        Person savedPerson = personRepository.save(person);
        Account account = new Account();
        // Creating default account for user with his id
//        account.setAccountNumber( 1000000000 + savedPerson.getId());
        String idString = String.valueOf(savedPerson.getId());
        String zeros = "0".repeat(13 - idString.length());
        String finalAccountNumber = zeros + idString;
        account.setAccountNumber(finalAccountNumber);

        // set shaba number
        //account.setShaba("IR100000000000000000000000" +savedPerson.getId());
        String shabaPrefix = "IR";
        String paddedZeros = "0".repeat(24 - idString.length());
        String shaba = shabaPrefix + paddedZeros + idString;
        account.setShaba(shaba);
        account.setCreationDate(LocalDate.now());
        // initial value to wallet just for welcome
        account.setAccountBalance(2000L);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        savedPerson.setAccount(accounts);
        return personRepository.save(savedPerson  );
    }

    public List<Person> findAll() {
        return personRepository.listOfExistingPeople();
    }
    public Person updatePersonInfo(Long id, Person person) {
        Person updatePerson = personRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found ! "));
        updatePerson.setEmail(person.getEmail());
        updatePerson.setPhoneNumber(person.getPhoneNumber());
        return personRepository.save(updatePerson);
    }

    public  void deleteUser(Long id) {
        Person deletePerson = personRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found ! "));
        deletePerson.setDeletedDate(String.valueOf(LocalDate.now()));
        personRepository.save(deletePerson);
    }


}