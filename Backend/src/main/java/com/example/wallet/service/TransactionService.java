package com.example.wallet.service;

import com.example.wallet.Exception.InsufficientBalance;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.security.JwtHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final PersonService personService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final JwtHelper jwtHelper ;

    public void createTransaction(Transaction transaction) {
        // getting person from authorization header
        String nationalId = jwtHelper.getUserNationalIdFromJWTWithoutUsingReq();
        //System.out.println(nationalId);
        Person person = personService.findByNationalId(nationalId);

        Account account = person.getAccount();

        // calculating transaction tax
        long tax = (long) (transaction.getTransactionAmount() * 0.0005);

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
//            finalAmount = transaction.getTransactionAmount() - transactionFee - tax ;
//            account.setAccountBalance(account.getAccountBalance() + finalAmount);
            // I understood , we should not have fee and tax for deposit
            transaction.setAccountBalanceAfterTransaction(account.getAccountBalance());
        } else {
            // calculate fee and tax
            finalAmount = transaction.getTransactionAmount() + transactionFee + tax ;

            // calculate daily transfer amount , and it's an assignment
            //  account.setDailyWithdrawAmount(account.getDailyWithdrawAmount() + finalAmount );
            // calculating this field without tax and fee
            account.setDailyWithdrawAmount(account.getDailyWithdrawAmount() + transaction.getTransactionAmount()  );

            if (account.getAccountBalance() - 10000 < finalAmount) {
                throw new InsufficientBalance("Insufficient balance");
            } else if (finalAmount > 10000000) {
                throw new InsufficientBalance("You have reach the limit of daily transaction amount");
            } else if (finalAmount < 100000) {
                throw new InsufficientBalance("Your transaction must be more than 100,000 rial");
            } else if (account.getDailyWithdrawAmount() > 10000000) {
                throw new RuntimeException("You have reached the maximum amount of daily withdraw");
            }
            account.setAccountBalance(account.getAccountBalance() - finalAmount);
            transaction.setAccountBalanceAfterTransaction(account.getAccountBalance());
        }
        transaction.setCreatedTime(LocalTime.now());
        transaction.setCreatedDate(LocalDate.now());
        if (account.getAccountTransactionList() == null) {
            account.setAccountTransactionList(new ArrayList<>()); // Initialize if null
        }
        account.getAccountTransactionList().add(transaction);
        accountRepository.save(account);
    }

    public List<Transaction> findAllTransactions() {
        // getting person from authorization header
        String nationalId = jwtHelper.getUserNationalIdFromJWTWithoutUsingReq();
        return transactionRepository.findAllTransactionsByNationalId(nationalId);
    }

    public void deleteTransaction(Long id) {
        Transaction deleteTransaction =  transactionRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Transaction not found ! "));
        deleteTransaction.setDeletedDate(String.valueOf(LocalDate.now()));
    }
    
    public List<Transaction> findAllDepositTransactions(){
        // getting person from authorization header
        String nationalId = jwtHelper.getUserNationalIdFromJWTWithoutUsingReq();
    	return transactionRepository.findAllDepositTransactions(nationalId);
    }

    public List<Transaction> findAllWithdrawTransactions(){
        // getting person from authorization header
        String nationalId = jwtHelper.getUserNationalIdFromJWTWithoutUsingReq();
    	return transactionRepository.findAllWithdrawTransactions(nationalId);
    }
}
