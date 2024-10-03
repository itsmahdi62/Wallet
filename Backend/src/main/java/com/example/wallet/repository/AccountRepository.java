package com.example.wallet.repository;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select o from account o where o.deletedDate is null")
    List<Account> listOfExistingAccounts();

    Optional<Account> findByAccountNumber(String accountNumber);
}
