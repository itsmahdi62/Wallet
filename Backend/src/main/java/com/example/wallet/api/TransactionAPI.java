package com.example.wallet.api;

import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import com.example.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transaction/")
@RequiredArgsConstructor
public class TransactionAPI {
    private final  TransactionService transactionService;
    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactionList = transactionService.findAllTransactions();
        if (transactionList == null || transactionList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(transactionList , HttpStatus.OK);
    }
    
    @PostMapping("/createTransaction")
    @ResponseBody
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction){
        transactionService.createTransaction(transaction);
        return new ResponseEntity<>("Transaction saved !" ,HttpStatus.CREATED);
    }

    @PostMapping("/deleteTransaction/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>("Transaction deleted" , HttpStatus.OK);
    }

    @GetMapping("/getAllDepositTransactions")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getAllDepositTransactions(){
    	List<Transaction> transactionList = transactionService.findAllDepositTransactions();
        if (transactionList == null || transactionList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(transactionList , HttpStatus.OK);
    }
//
    @GetMapping("/getAllWithdrawTransactions")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getAllWithdrawTransactions(){
    	List<Transaction> transactionList = transactionService.findAllWithdrawTransactions();
        if (transactionList == null || transactionList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if the list is empty
        }
        return new ResponseEntity<>(transactionList , HttpStatus.OK);
    }
    // write sort and select transaction by time and etc

}
