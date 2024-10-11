package com.example.wallet.repository;

import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM person p " +
            "JOIN p.account a " +
            "JOIN a.accountTransactionList t " +
            "WHERE p.nationalId = :nationalId and t.deletedDate is null")
    List<Transaction> findAllTransactionsByNationalId(@Param("nationalId") String nationalId);

    @Query("SELECT t FROM person p " +
            "JOIN p.account a " +
            "JOIN a.accountTransactionList t " +
            "WHERE p.nationalId = :nationalId and t.isDeposit = true  and t.deletedDate is null ")
    List<Transaction> findAllDepositTransactions(@Param("nationalId") String nationalId);

    @Query("SELECT t FROM person p " +
            "JOIN p.account a " +
            "JOIN a.accountTransactionList t " +
            "WHERE p.nationalId = :nationalId and t.isDeposit = false  and t.deletedDate is null ")
    List<Transaction> findAllWithdrawTransactions(@Param("nationalId") String nationalId);


}
