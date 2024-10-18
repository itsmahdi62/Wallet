package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.security.JwtHelper;
import com.example.wallet.service.EmailService;
import com.example.wallet.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1/person/")
@RequiredArgsConstructor
@Slf4j
public class PersonAPI {
    private final  PersonService personService;
    private final JwtHelper jwtHelper ;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Person>> getAllPeople() {
        List<Person> personList = personService.findAllPeople(); // Call to personService to fetch all users
        if (personList == null || personList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(personList, HttpStatus.OK); // Return the list with 200 OK status
    }
    @PostMapping("/signup" )
    @ResponseBody
    public ResponseEntity<Map> createPerson(@Valid @RequestBody Person newperson) throws Exception {
       //  if we had person and admin roles in our entity this line of code would let anyone  be an admin
        Person person = new Person();
        person.setNationalId(newperson.getNationalId());
        person.setName(newperson.getName());
        person.setFamily(newperson.getFamily());
        person.setPhoneNumber(newperson.getPhoneNumber());
        person.setDayOfBirth(newperson.getDayOfBirth());
        person.setMonthOfBirth(newperson.getMonthOfBirth());
        person.setYearOfBirth(newperson.getYearOfBirth());
        person.setEmail(newperson.getEmail());
        person.setIsMale(newperson.getIsMale());

        int newPersonAge = LocalDate.now().getYear() - newperson.getYearOfBirth() ;
        if(newperson.getIsMale() && newPersonAge >= 18){
            if(newperson.getMilitaryServiceStatus() == null){
                throw new IllegalArgumentException("Military service status must be provided for males 18 years or older.");
            }
        }
        person.setMilitaryServiceStatus(newperson.getMilitaryServiceStatus());

        Person savedPerson = personService.savePerson(person);
        String token = jwtHelper.generateToken(savedPerson);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("person", savedPerson); // Add savedPerson object
        responseBody.put("token", token);        // Add the token
        return   new ResponseEntity<>(responseBody , headers, HttpStatus.CREATED);
    }

    @PostMapping("/update-user-info/{id}")
    @ResponseBody
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        Person updatedPerson = personService.updatePersonInfo(id, person);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @PostMapping("/delete-user/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable Long id ){
        personService.deleteUser(id);
        return new ResponseEntity<>("Person deleted ! " , HttpStatus.OK);
    }

    @GetMapping("/findOneById/{id}")
    @ResponseBody
    public  ResponseEntity<Person> findPersonById(@PathVariable Long id ){
        Person person = personService.findOneById(id); // Call to personService to fetch all users
        if (person == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
}
