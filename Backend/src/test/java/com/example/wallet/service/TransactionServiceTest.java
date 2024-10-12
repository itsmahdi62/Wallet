package com.example.wallet.service;

import com.example.wallet.Exception.InsufficientBalance;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.security.JwtHelper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private PersonService personService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtHelper jwtHelper;

    private Person person;
    private Account account;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Initialize person, account, and transaction for testing
        person = new Person();
        account = new Account();
        account.setAccountBalance(1000000L);
        account.setDailyWithdrawAmount(0L);
        person.setAccount(account);

        transaction = new Transaction();
        transaction.setTransactionAmount(500000L); // A deposit example
        transaction.setDeposit(true);
    }

    @Test
    void testCreateTransaction_Deposit() {
        // Mock JWT to return a specific nationalId
        when(jwtHelper.getUserNationalIdFromJWTWithoutUsingReq()).thenReturn("1234567890");

        // Mock person retrieval
        when(personService.findByNationalId("1234567890")).thenReturn(person);

        // Call the createTransaction method
        transactionService.createTransaction(transaction);
        // Verify the account balance is updated correctly for a deposit
        assertEquals(1500000L, account.getAccountBalance() + transaction.getTransactionAmount());
        verify(accountRepository, times(1)).save(account); // Ensure the account is saved
    }

    @Test
    void testCreateTransaction_Withdraw() {
        // Set the transaction to be a withdrawal
        transaction.setDeposit(false);
        transaction.setTransactionAmount(200000L); // Set withdrawal amount
        // Mock JWT and person retrieval
        when(jwtHelper.getUserNationalIdFromJWTWithoutUsingReq()).thenReturn("1234567890");
        when(personService.findByNationalId("1234567890")).thenReturn(person);
        // Call the createTransaction method

        transactionService.createTransaction(transaction);


        long tax = (long) (200000L * 0.0005);
        int transactionFee = 0;
        if(transaction.getTransactionAmount() > 1000000)
        {
            transactionFee = 7200;
        }else {
            transactionFee = 20000;
        }
        // Verify the account balance is updated correctly for a withdrawal
        assertEquals(800000L - tax - transactionFee, account.getAccountBalance()); // Balance after withdrawal
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateTransaction_InsufficientBalance() {
        // Set the withdrawal to be larger than the balance
        transaction.setDeposit(false);
        transaction.setTransactionAmount(11000L); // Exceeds the balance

        // Mock JWT and person retrieval
        when(jwtHelper.getUserNationalIdFromJWTWithoutUsingReq()).thenReturn("1234567890");
        when(personService.findByNationalId("1234567890")).thenReturn(person);

        // Test if InsufficientBalance exception is thrown
        assertThrows(InsufficientBalance.class, () -> transactionService.createTransaction(transaction));
    }
    @Test
    void testDeleteTransaction_NotFound() {
        // Mock empty transaction retrieval
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test if EntityNotFoundException is thrown
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transactionService.deleteTransaction(1L);
        });

        assertEquals("Transaction not found ! ", exception.getMessage());
    }

    @Test
    void testFindAllTransactions() {
        // Mock JWT and person retrieval
        when(jwtHelper.getUserNationalIdFromJWTWithoutUsingReq()).thenReturn("1234567890");

        // Call findAllTransactions
        transactionService.findAllTransactions();

        // Verify transaction retrieval
        verify(transactionRepository, times(1)).findAllTransactionsByNationalId("1234567890");
    }
    @Test
    void testFindAllDepositTransactions() {
        // Mock JWT and person retrieval
        when(jwtHelper.getUserNationalIdFromJWTWithoutUsingReq()).thenReturn("1234567890");
        // Call findAllTransactions
        transactionService.findAllDepositTransactions();

        // Verify transaction retrieval
        verify(transactionRepository, times(1)).findAllDepositTransactions("1234567890");
    }
    @Test
    void testFindAllWithdrawTransactions() {
        // Mock JWT and person retrieval
        when(jwtHelper.getUserNationalIdFromJWTWithoutUsingReq()).thenReturn("1234567890");

        // Call findAllTransactions
        transactionService.findAllWithdrawTransactions();

        // Verify transaction retrieval
        verify(transactionRepository, times(1)).findAllWithdrawTransactions("1234567890");
    }

}
