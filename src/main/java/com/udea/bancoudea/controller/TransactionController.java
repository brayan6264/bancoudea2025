package com.udea.bancoudea.controller;

import com.udea.bancoudea.DTO.CustomerDTO;
import com.udea.bancoudea.service.CustomerService;
import com.udea.bancoudea.service.TransactionService;
import com.udea.bancoudea.DTO.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionFacade;
    //Constructor
    public TransactionController(TransactionService transactionFacade) {
        this.transactionFacade = transactionFacade;
    }
    //Get all transactions for account number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(transactionFacade.getTransactionsForAccount(accountNumber));
    }
    //Create New transaction
    @PostMapping
    public ResponseEntity<TransactionDTO> transferMoney(@RequestBody TransactionDTO transactionDTO){
        return ResponseEntity.ok(transactionFacade.transferMoney(transactionDTO));
    }
}
