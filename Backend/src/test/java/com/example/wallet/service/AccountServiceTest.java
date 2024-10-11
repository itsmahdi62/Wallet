package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = Account.builder()
                .accountNumber("1234567890123")
                .accountBalance(5000L)
                .creationDate(LocalDate.now())
                .build();
    }

    @Test
    void createAccount() {
        Person person = new Person();
        person.setId(1L);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(person);
        assertNotNull(createdAccount);
        assertEquals("10000000001", createdAccount.getAccountNumber());
        assertEquals("IR000000000000000000000001", createdAccount.getShaba());
        assertEquals(2000L, createdAccount.getAccountBalance());
    }

    @Test
    void updateAccountInfo() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.updateAccountInfo(1L, account);
        assertNotNull(updatedAccount);
        assertEquals("1234567890123", updatedAccount.getAccountNumber());
    }

    @Test
    void deleteAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);
        assertNotNull(account.getDeletedDate());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void updateAccountNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountService.updateAccountInfo(1L, account));
        assertEquals("Person not found !", exception.getMessage());
    }
}
