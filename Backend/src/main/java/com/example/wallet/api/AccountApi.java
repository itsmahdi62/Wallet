package com.example.wallet.api;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Person;
import com.example.wallet.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/account/")
@RequiredArgsConstructor
public class AccountApi {
    AccountService accountService ;

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<Account>> findAll() {
        List<Account> accountList = accountService.findAllAcounts();
        if (accountList == null || accountList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(accountList, HttpStatus.OK); // Return the list with 200 OK status
    }
    @PostMapping("/createAccount")
    @ResponseBody
    public ResponseEntity<Account> save(@Valid @RequestBody Person currentUser) {

        return  new ResponseEntity<>(accountService.createAccount(currentUser), HttpStatus.OK);
    }

    @PostMapping("/update-account-info/{id}")
    @ResponseBody
    public ResponseEntity<Person> updateAccountInfo(@PathVariable Long id,@Valid @RequestBody Account account) {
        Person updatedPerson = accountService.updateAccountInfo(id, account);
//        return ResponseEntity.ok(updatedPerson);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @PostMapping("/delete-account/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable Long id ){
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account deleted ! " , HttpStatus.OK);
    }

}
