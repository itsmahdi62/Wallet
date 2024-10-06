package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PersonService personService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void createTransaction(Transaction transaction) {
        // getting user from authorization header
        String nationalId = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personService.findByNationalId(nationalId);

        Account account = person.getAccount();

        // calculating transaction tax
        long tax = (long) (transaction.getTransactionAmount() * 0.05);

        // calculating transaction fee
        int transactionFee = 0;
        if(transaction.getTransactionAmount() > 1000000)
        {
            transactionFee = 7200;
        }else {
            transactionFee = 20000;
        }
        long finalAmount ;

        // calculating account balance after transaction
        // wow !!! in lombok for boolean variable we say transaction.isDeposit() instead of transaction.getIsDeposit()
        // validating transaction
        if (transaction.isDeposit()) {
            finalAmount = transaction.getTransactionAmount() - transactionFee - tax ;
            account.setAccountBalance(account.getAccountBalance() + finalAmount);
        } else {
            finalAmount = transaction.getTransactionAmount() + transactionFee + tax ;
            if (account.getAccountBalance() - 10000 < finalAmount) {
                throw new RuntimeException("Insufficient balance");
            } else if (finalAmount > 10000000) {
                throw new RuntimeException("You have reach the limit of daily transaction amount");
            } else if (finalAmount < 100000) {
                throw new RuntimeException("Your transaction must be more than 100,000 rial");
            }
            account.setAccountBalance(account.getAccountBalance() - finalAmount);
        }


        transaction.setTime(LocalTime.now());
        transaction.setDate(LocalDate.now());
        if (account.getAccountTransactionList() == null) {
            account.setAccountTransactionList(new ArrayList<>()); // Initialize if null
        }
        account.getAccountTransactionList().add(transaction);
        account.setDailyTransferAmount(0);
        accountRepository.save(account);
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

}
