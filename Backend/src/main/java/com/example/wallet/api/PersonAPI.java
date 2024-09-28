package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/person/")
@RequiredArgsConstructor
public class PersonAPI {

    private final PersonService personService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Person>> findAll() {
        List<Person> personList = personService.findAll(); // Call to personService to fetch all users
        if (personList == null || personList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(personList, HttpStatus.OK); // Return the list with 200 OK status
    }
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Person> save(@Valid @RequestBody Person newperson) {
       //  if we had user and admin roles in our entity this line of code would let anyone to be an admin
        Person person = new Person();
        person.setPersonId(newperson.getPersonId());
        person.setName(newperson.getName());
        person.setFamily(newperson.getFamily());
        person.setAge(newperson.getAge());
        person.setPhoneNumber(newperson.getPhoneNumber());
        person.setDateOfBirth(newperson.getDateOfBirth());
        person.setEmail(newperson.getEmail());
        person.setIsMail(newperson.getIsMail());
        if(newperson.getIsMail() && newperson.getAge() >= 18){
            if(newperson.getMilitaryServiceStatus() == null){
                throw new IllegalArgumentException("Military service status must be provided for males 18 years or older.");
            }
        }
        person.setMilitaryServiceStatus(newperson.getMilitaryServiceStatus());
        return   new ResponseEntity<>(personService.savePerson(person), HttpStatus.OK);
    }

    @PostMapping("/update-use-info/{id}")
    @ResponseBody
    public ResponseEntity<Person> updateUserInfo(@PathVariable Long id,@Valid @RequestBody Person person) {
        Person updatedPerson = personService.updatePersonInfo(id, person);
//        return ResponseEntity.ok(updatedPerson);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @PostMapping("/delete-user/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable Long id ){
        personService.deleteUser(id);
        return new ResponseEntity<>("User deleted ! " , HttpStatus.OK);
    }
}
