package com.example.wallet.repository;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {


}
