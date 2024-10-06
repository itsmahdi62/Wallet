package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.security.repository.AccountRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AccountResetService {

    @Autowired
    private AccountRepository accountRepository;  // Assuming you have a repository for Account

    @Scheduled(cron = "0 0 0 * * ?")  // Executes every day at 00:00 AM
    public void resetDailyTransferAmount() {
        // Reset dailyTransferAmount for all accounts
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            account.setDailyTransferAmount(0L);
        }
        accountRepository.saveAll(accounts);  // Save the changes
    }
}

