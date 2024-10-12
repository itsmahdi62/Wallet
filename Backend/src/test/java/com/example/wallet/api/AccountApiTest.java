package com.example.wallet.api;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountApiTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountAPI accountAPI;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = Account.builder()
                .accountNumber("1234567890123")
                .accountBalance(5000L)
                .build();
    }

    @Test
    void getAllAccounts() {
        when(accountService.findAllAccounts()).thenReturn(List.of(account));

        ResponseEntity<List<Account>> response = accountAPI.getAllAccounts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllAccountsNoContent() {
        when(accountService.findAllAccounts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Account>> response = accountAPI.getAllAccounts();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void createAccount() {
        Person person = new Person();
        person.setId(1L);

        when(accountService.createAccount(any(Person.class))).thenReturn(account);

        ResponseEntity<Account> response = accountAPI.createAccount(person);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1234567890123", response.getBody().getAccountNumber());
    }

    @Test
    void deleteAccount() {
        ResponseEntity<String> response = accountAPI.deleteAccount(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountService, times(1)).deleteAccount(1L);
    }
}
