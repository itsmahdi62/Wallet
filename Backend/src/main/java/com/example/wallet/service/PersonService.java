package com.example.wallet.service;

import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    // in spring boot best practice is to use inject by constructor instead of autowired
    private final PersonRepository personRepository;


    public Person savePerson(Person person) {
        //  !person.getIsMail())

        return personRepository.save(person);
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