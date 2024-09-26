package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/person/")
//@RequiredArgsConstructor
public class PersonAPI {

    private final PersonService personService;

    public PersonAPI(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping("/getAllUsers")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/getAllUserstest")
    public ResponseEntity<List<Person>> findAlltest() {
        List<Person> personList = personService.findAll(); // Call to personService to fetch all users
        if (personList == null || personList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(personList, HttpStatus.OK); // Return the list with 200 OK status
        // Create a response body
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "success");
//        response.put("data", Collections.singletonMap("user", updatedUser));
//
//        // Return ResponseEntity with status 200
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/signup")
    @ResponseBody
    public Object save(@Valid @RequestBody Person newperson) {
        // if we had user and admin roles in our entity this line of code would let anyone to be an admin
        //        return personService.savePerson(person);
//        Person person = new Person();
//        person.setPersonId(newperson.getPersonId());
//        person.setName(newperson.getName());
//        person.setFamily(newperson.getFamily());
//        person.setPhoneNumber(newperson.getPhoneNumber());
//        person.setDateOfBirth(newperson.getDateOfBirth());
//        person.setEmail(newperson.getEmail());
//        person.setIsMail(newperson.getIsMail());
//        if(newperson.getIsMail()) {
//        }
        System.out.println(newperson.getEmail());
        return  personService.savePerson(newperson);
    }

}
