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
class AccountServiceTest {

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


}
