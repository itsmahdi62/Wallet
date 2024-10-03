package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PersonService personService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void createTransaction(Long amount, String transactionType) {
        // گرفتن کاربر فعلی از JWT
        String nationalId = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personService.findByNationalId(nationalId);

        // بازیابی حساب کاربر
        Account account = person.getAccount();

        // محاسبه موجودی بعد از تراکنش
        long accountBalanceAfterTransaction;
        if ("deposit".equals(transactionType)) {
            accountBalanceAfterTransaction = account.getAccountBalance() + amount;
        } else if ("withdraw".equals(transactionType)) {
            if (account.getAccountBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }
            accountBalanceAfterTransaction = account.getAccountBalance() - amount;
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        // ایجاد تراکنش جدید
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setDailyTransactionAmount(amount);
        transaction.setAccountBalanceAfterTransaction(accountBalanceAfterTransaction);
        transaction.setTime(LocalTime.now());
        transaction.setDate(LocalDate.now());
        transactionRepository.save(transaction);

        // به‌روزرسانی موجودی حساب
        account.setAccountBalance(accountBalanceAfterTransaction);
        accountRepository.save(account);
    }
}
