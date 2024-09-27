package com.example.wallet.service;

import com.example.wallet.entity.Person;
import com.example.wallet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    // in spring boot best practice is to use inject by constructor instead of autowired
    private final PersonRepository personRepository;


    public Object savePerson(Person person) {
        //  !person.getIsMail())
        if (Boolean.FALSE.equals(person.getIsMail())) {
            person.setMilitaryServiceStatus(null); // Clear military service if not male
        }
        return personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }
}