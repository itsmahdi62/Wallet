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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks and injects them
    }

    @Test
    void createAccount_ShouldCreateDefaultAccount() {
        // Given
        Person person = new Person();
        person.setId(1L);

        // When
        Account createdAccount = accountService.createAccount(person);

        // Then
        assertNotNull(createdAccount);
        assertEquals("1000000000001", createdAccount.getAccountNumber());
        assertEquals("IR000000000000000000000001", createdAccount.getShaba());
        assertEquals(2000L, createdAccount.getAccountBalance());
        assertEquals(LocalDate.now(), createdAccount.getCreationDate());
    }

    @Test
    void findAllAccounts_ShouldReturnListOfAccounts() {
        // Given
        Account account1 = new Account();
        account1.setAccountNumber("100000000001");
        Account account2 = new Account();
        account2.setAccountNumber("100000000002");

        when(accountRepository.listOfExistingAccounts()).thenReturn(Arrays.asList(account1, account2));

        // When
        List<Account> accounts = accountService.findAllAccounts();

        // Then
        assertEquals(2, accounts.size());
        assertEquals("100000000001", accounts.get(0).getAccountNumber());
        assertEquals("100000000002", accounts.get(1).getAccountNumber());
        verify(accountRepository, times(1)).listOfExistingAccounts();
    }

    @Test
    void updateAccountInfo_ShouldUpdateAccount() {
        // Given
        Long accountId = 1L;
        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setAccountNumber("100000000001");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        Account updatedAccount = new Account();
        updatedAccount.setAccountBalance(5000L);

        // Mock the save operation to return the updated account
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        // When
        Account result = accountService.updateAccountInfo(accountId, updatedAccount);

        // Then
        assertNotNull(result);
        assertEquals(5000L, result.getAccountBalance());
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void updateAccountInfo_ShouldThrowExceptionWhenAccountNotFound() {
        // Given
        Long accountId = 1L;
        Account updatedAccount = new Account();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> accountService.updateAccountInfo(accountId, updatedAccount));
    }

    @Test
    void deleteAccount_ShouldMarkAccountAsDeleted() {
        // Given
        Long accountId = 1L;
        Account existingAccount = new Account();
        existingAccount.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        // When
        accountService.deleteAccount(accountId);

        // Then
        assertNotNull(existingAccount.getDeletedDate());
        assertEquals(String.valueOf(LocalDate.now()), existingAccount.getDeletedDate());
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void deleteAccount_ShouldThrowExceptionWhenAccountNotFound() {
        // Given
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> accountService.deleteAccount(accountId));
    }
}
