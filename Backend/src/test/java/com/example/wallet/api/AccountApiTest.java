package com.example.wallet.api;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.PersonEnums.MilitaryServiceStatus;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountApiTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountAPI accountAPI;

    @Mock
    private AccountRepository accountRepository ;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and set up MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountAPI).build();
    }

    @Test
    void testGetAllAccounts() throws Exception {
        // Mock the data
        Account account1 = new Account();
        account1.setId(1L);
        account1.setAccountNumber("1000000000001");
        account1.setShaba("IR0000000000000000000000001");
        account1.setAccountBalance(5000L);
        account1.setCreationDate(LocalDate.now());

        Account account2 = new Account();
        account2.setId(2L);
        account2.setAccountNumber("1000000000002");
        account2.setShaba("IR0000000000000000000000002");
        account2.setAccountBalance(7000L);
        account2.setCreationDate(LocalDate.now());

        List<Account> accounts = Arrays.asList(account1, account2);

        // Simulate service response
        when(accountService.findAllAccounts()).thenReturn(accounts);

        // Perform the test request and validate the response
        mockMvc.perform(get("/api/v1/account/getAllAccounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("1000000000001"))
                .andExpect(jsonPath("$[1].accountNumber").value("1000000000002"));
    }
    @Test
    void testCreateAccount() throws Exception {
        // Create and mock the Person object
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
        person.setMilitaryServiceStatus(MilitaryServiceStatus.valueOf("COMPLETED"));
        person.setIsMale(true);

        // Create and mock the Account object
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("1000000000001"); // Expected account number
        account.setShaba("IR0000000000000000000000001"); // Expected SHABA number
        account.setAccountBalance(2000L);
        account.setCreationDate(LocalDate.now());

        // Simulate service response
        when(accountService.createAccount(any(Person.class))).thenReturn(account);

        // Perform the test request and validate the response
        mockMvc.perform(post("/api/v1/account/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Mahdi\",\"family\":\"Almasi\"," +
                                "\"nationalId\":\"3920841271\"," +
                                "\"email\":\"amiralmasi021@gmail.com\"," +
                                "\"phoneNumber\":\"09391395538\"," +
                                "\"yearOfBirth\":2001," +
                                "\"monthOfBirth\":4," +
                                "\"dayOfBirth\":22," +
                                "\"militaryServiceStatus\":\"COMPLETED\"," +
                                "\"isMale\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1000000000001")); // Assert the expected account number
    }
// this test does not work properly
    @Test
    void testUpdateAccount() throws Exception {
        // Create the existing Account object to be updated
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setAccountNumber("1000000000001");
        existingAccount.setShaba("IR0000000000000000000000001");
        existingAccount.setAccountBalance(2000L);
        existingAccount.setCreationDate(LocalDate.now());

        // Create the updated Account object with new balance
        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber("1000000000001"); // Keep the same account number
        updatedAccount.setShaba("IR0000000000000000000000001"); // Keep the same SHABA number
        updatedAccount.setAccountBalance(3000L); // New balance
        updatedAccount.setCreationDate(LocalDate.now());

        // Mock the repository to return the existing account when searched by ID
        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        // Mock the service to return the updated account
        when(accountService.updateAccountInfo(1L, updatedAccount)).thenReturn(updatedAccount);

        // Perform the test request and validate the response
        mockMvc.perform(post("/api/v1/account/update-account-info/1") // Use the account ID in the URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"1000000000001\"," +
                                "\"shaba\":\"IR000000000000000000000001\"," +
                                "\"accountBalance\":3000," +
                                "\"creationDate\":\"" + LocalDate.now() + "\"}")) // JSON with updated values
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(3000L)); // Assert the updated account balance
    }


    @Test
    void testDeleteAccount() throws Exception {
        // Perform the test request and validate the response
        mockMvc.perform(post("/api/v1/account/delete-account/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted ! "));
    }
}
