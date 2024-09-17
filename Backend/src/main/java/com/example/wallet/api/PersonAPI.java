package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/person/")
@RequiredArgsConstructor
public class PersonAPI {
    private final PersonService personService;

    @GetMapping("/getAllUsers")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping("/signup")
    @ResponseBody
    public Object save(@RequestBody Person newperson) {
        // if we had user and admin roles in our entity this line of code would let anyone to be an admin
        //        return personService.savePerson(person);
        Person person = new Person();
        person.setPersonId(newperson.getPersonId());
        person.setName(newperson.getName());
        person.setFamily(newperson.getFamily());
        person.setPhoneNumber(newperson.getPhoneNumber());
        person.setDateOfBirth(newperson.getDateOfBirth());
        person.setEmail(newperson.getEmail());
        person.setIsMail(newperson.getIsMail());
        if(newperson.getIsMail()) {
        }
        return  personService.savePerson(person);
    }

}
