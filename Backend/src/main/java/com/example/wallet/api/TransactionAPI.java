package com.example.wallet.api;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import com.example.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/transaction/")
@RequiredArgsConstructor
public class TransactionAPI {
    private TransactionService transactionService;
    @GetMapping("/getAll")
    public ResponseEntity<List<Transaction>> getListOfTransactions(){
        List<Transaction> transactionList = transactionService.findAllTransactions();
        if (transactionList == null || transactionList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(transactionList , HttpStatus.OK);
    }
}
