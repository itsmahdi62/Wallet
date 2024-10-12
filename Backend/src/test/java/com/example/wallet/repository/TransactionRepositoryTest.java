//package com.example.wallet.repository;
//
//import com.example.wallet.entity.Transaction;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@DataJpaTest
//@ActiveProfiles("test")
//public class TransactionRepositoryTest {
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Test
//    void testFindAllTransactionsByNationalId(){
//        Transaction transaction1 = Transaction.builder()
//                .createdDate(LocalDate.now())
//                .createdTime(LocalTime.now())
//                .transactionAmount(500000L)
//                .accountBalanceAfterTransaction(700000L)
//                .isDeposit(true)
//                .build();
//        Transaction transaction2 = Transaction.builder()
//                .createdDate(LocalDate.now())
//                .createdTime(LocalTime.now())
//                .transactionAmount(50000L)
//                .accountBalanceAfterTransaction(70000L)
//                .isDeposit(false)
//                .build();
//        transactionRepository.save(transaction1);
//        transactionRepository.save(transaction2);
//        List<Transaction> transactionList = transactionRepository.findAllTransactionsByNationalId("3920841271");
//        Assertions.assertThat(transactionList).isNotNull();
//    }
//
//}
