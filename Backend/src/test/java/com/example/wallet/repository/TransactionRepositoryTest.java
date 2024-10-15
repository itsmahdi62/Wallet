package com.example.wallet.repository;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional // Ensures that each test runs in a transaction that gets rolled back
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Person person;
    private Account account;

    @BeforeEach
    void setUp() {
        // Create and persist a Person
        person = Person.builder()
                .nationalId("1234567890")
                .phoneNumber("09391395538")
                .isMale(false)
                .email("amir@gmail.com")
                .build();
        entityManager.persist(person);

        // Create and persist an Account linked to the Person
        account = Account.builder()
                .accountNumber("1000000000000")
                .accountBalance(2000L)
                .creationDate(LocalDate.now())
                .build();
        entityManager.persist(account);

        // Link account to person if necessary (this part may depend on your setup)
        person.setAccount(account); // Assuming there's a setter in Person class
        entityManager.persist(person); // Update the person with the account link
    }

    @Test
    void testFindAllTransactionsByNationalId() {
        // Create and persist a Transaction linked to the Person
        Transaction transaction = Transaction.builder()
                .createdTime(LocalTime.now())
                .createdDate(LocalDate.now())
                .transactionAmount(200000L)
                .isDeposit(true)
                .deletedDate(null)
                .build();

        account.setAccountTransactionList(List.of(transaction));
        entityManager.persist(transaction);
        entityManager.persist(account); // Ensure the account is saved with the transaction

        // Test the repository method
        List<Transaction> transactions = transactionRepository.findAllTransactionsByNationalId("1234567890");

        // Assertions
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getTransactionAmount()).isEqualTo(200000L);
    }

    @Test
    void testFindAllDepositTransactions() {
        // Create and persist a deposit transaction
        Transaction depositTransaction = Transaction.builder()
                .createdTime(LocalTime.now())
                .createdDate(LocalDate.now())
                .transactionAmount(150000L)
                .isDeposit(true)
                .deletedDate(null)
                .build();

        account.setAccountTransactionList(List.of(depositTransaction));
        entityManager.persist(depositTransaction);
        entityManager.persist(account); // Ensure the account is saved with the transaction

        // Test the repository method
        List<Transaction> depositTransactions = transactionRepository.findAllDepositTransactions("1234567890");

        // Assertions
        assertThat(depositTransactions).isNotEmpty();
        assertThat(depositTransactions).hasSize(1);
        assertThat(depositTransactions.get(0).getTransactionAmount()).isEqualTo(150000L);
    }

    @Test
    void testFindAllWithdrawTransactions() {
        // Create and persist a withdrawal transaction
        Transaction withdrawTransaction = Transaction.builder()
                .createdTime(LocalTime.now())
                .createdDate(LocalDate.now())
                .transactionAmount(100000L)
                .isDeposit(false)
                .deletedDate(null)
                .build();

        account.setAccountTransactionList(List.of(withdrawTransaction));
        entityManager.persist(withdrawTransaction);
        // Ensure the account is saved with the transaction
        entityManager.persist(account);

        // Test the repository method
        List<Transaction> withdrawTransactions = transactionRepository.findAllWithdrawTransactions("1234567890");

        // Assertions
        assertThat(withdrawTransactions).isNotEmpty();
        assertThat(withdrawTransactions).hasSize(1);
        assertThat(withdrawTransactions.get(0).getTransactionAmount()).isEqualTo(100000L);
    }
}
