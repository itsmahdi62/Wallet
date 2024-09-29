package com.example.wallet.service;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository ;
    public Account createAccount(Person savedPerson){
        // Creating default account for user with his id
        Account account = new Account();

        // Set account number
        // Account.setAccountNumber( 1000000000 + savedPerson.getId()); wrong way
        String idString = String.valueOf(savedPerson.getId());
        String zeros = "0".repeat(13 - idString.length());
        String finalAccountNumber = zeros + idString;
        account.setAccountNumber(finalAccountNumber);

        // set shaba number
        //account.setShaba("IR100000000000000000000000" +savedPerson.getId());
        String shabaPrefix = "IR";
        String paddedZeros = "0".repeat(24 - idString.length());
        String shaba = shabaPrefix + paddedZeros + idString;
        account.setShaba(shaba);
        account.setCreationDate(LocalDate.now());
        // initial value to wallet just for welcome
        account.setAccountBalance(2000L);
        return account ;
    }

    public List<Account> findAllAcounts(){
        return accountRepository.listOfExistingAccounts();
    }

    public Account updateAccountInfo(Long id, Person person) {
        Account updateAccount = accountRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found ! "));
//        updateAccount.setEmail(person.getEmail());
//        updateAccount.setPhoneNumber(person.getPhoneNumber());
        return accountRepository.save(updateAccount);
    }

    public void deleteAccount(Long id) {
        Account deleteAccount = accountRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found ! "));
        deleteAccount.setDeletedDate(String.valueOf(LocalDate.now()));
        accountRepository.save(deleteAccount);
    }


}
