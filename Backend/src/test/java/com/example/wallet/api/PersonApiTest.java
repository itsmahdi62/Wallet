package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.security.JwtHelper;
import com.example.wallet.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PersonApiTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @Mock
    private JwtHelper jwtHelper;

    @InjectMocks
    private PersonAPI personAPI;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and set up MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(personAPI).build();
    }

    @Test
    void testGetAllPeople() throws Exception {
        // Mock the data
        Person person1 = new Person();
        person1.setName("Mahdi");
        person1.setFamily("Almasi");
        person1.setNationalId("3920841271");

        Person person2 = new Person();
        person2.setName("Mohammad");
        person2.setFamily("Mohammadi");
        person2.setNationalId("0987654321");

        // Simulate service response
        // whenever the personService.findAllPeople() method is called during the test,
        // it should return a predefined list containing person1 and person2
        when(personService.findAllPeople()).thenReturn(Arrays.asList(person1, person2));

        // Perform the test request and validate the response
        mockMvc.perform(get("/api/v1/person/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mahdi"))
                .andExpect(jsonPath("$[1].name").value("Mohammad"));
    }

    @Test
    void testCreatePerson() throws Exception {
        // Mock person data
        Person person = new Person();
        person.setId(1L);
        person.setName("Mahdi");
        person.setFamily("Almasi");
        person.setNationalId("3920841271");
        person.setEmail("amiralmasi021@gmail.com");
        person.setPhoneNumber("09391395538");
        person.setYearOfBirth(2001);
        person.setMonthOfBirth(4);
        person.setDayOfBirth(22);
//        person.setMilitaryServiceStatus("COMPLETED");
        person.setIsMale(true);

        // Simulate the service and token generation
        when(personService.savePerson(any(Person.class))).thenReturn(person);
        when(jwtHelper.generateToken(any(Person.class))).thenReturn("dummyToken");

        // Prepare the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Mahdi");
        requestBody.put("family", "Almasi");
        requestBody.put("email" , "amiralmasi021@gmail.com");
        requestBody.put("phoneNumber" , "09391395538");
        requestBody.put("nationalId", "3920841271");
        requestBody.put("militaryServiceStatus" , "COMPLETED");
        requestBody.put("yearOfBirth", 2001);
        requestBody.put("monthOfBirth", 4);
        requestBody.put("dayOfBirth", 22);
        requestBody.put("isMale", true);

        // Perform the test request and validate the response
        mockMvc.perform(post("/api/v1/person/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Authorization", "Bearer dummyToken"))
                .andExpect(jsonPath("$.person.name").value("Mahdi"))
                .andExpect(jsonPath("$.token").value("dummyToken"));
    }

    @Test
    void testUpdatePerson() throws Exception {
        // Mock updated person data
        Person updatedPerson = new Person();
        updatedPerson.setName("Updated Name");
        updatedPerson.setEmail("updatedemail@example.com");

        // Simulate the service response
        when(personService.updatePersonInfo(any(Long.class), any(Person.class))).thenReturn(updatedPerson);

        // Perform the test request and validate the response
        mockMvc.perform(post("/api/v1/person/update-user-info/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updatedemail@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Perform the delete request and validate the response
        mockMvc.perform(post("/api/v1/person/delete-user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person deleted ! "));
    }

    @Test
    void testFindPersonById() throws Exception {
        // Mock person data
        Person person = new Person();
        person.setName("Mahdi");
        person.setFamily("Almasi");

        // Simulate the service response
        when(personService.findOneById(1L)).thenReturn(person);

        // Perform the test request and validate the response
        mockMvc.perform(get("/api/v1/person/findOneById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mahdi"))
                .andExpect(jsonPath("$.family").value("Almasi"));
    }
}
