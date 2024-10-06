package com.example.wallet.security.repository;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {
    @Query("select o from transaction o where o.deletedDate is null")
    List<Transaction> listOfExistingTransactions();


}
