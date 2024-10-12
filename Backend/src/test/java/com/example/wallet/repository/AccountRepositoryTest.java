package com.example.wallet.repository;

import com.example.wallet.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .accountNumber("1234567890123")
                .accountBalance(5000L)
                .shaba("IR000000000000000000000012")
                .build();
        accountRepository.save(account);
    }

    @Test
    @Rollback(value = false)
    void testFindByAccountNumber() {
        Optional<Account> foundAccount = accountRepository.findByAccountNumber("1234567890123");
        assertThat(foundAccount.isPresent()).isTrue();
        assertThat(foundAccount.get().getAccountNumber()).isEqualTo("1234567890123");
    }

    @Test
    void testListOfExistingAccounts() {
        List<Account> accounts = accountRepository.listOfExistingAccounts();
        assertThat(accounts).isNotEmpty();
        assertThat(accounts).hasSize(1);
    }
}
