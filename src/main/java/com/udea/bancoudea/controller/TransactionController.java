package com.udea.bancoudea.controller;

import com.udea.bancoudea.DTO.CustomerDTO;
import com.udea.bancoudea.DTO.TransferRequestDTO;
import com.udea.bancoudea.service.CustomerService;
import com.udea.bancoudea.service.TransactionService;
import com.udea.bancoudea.DTO.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionFacade;
    //Constructor
    public TransactionController(TransactionService transactionFacade) {
        this.transactionFacade = transactionFacade;
    }

    //d 6.1 LISTO
    //OBTENER TODAS LAS TRANSACCIONES
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(){
        return ResponseEntity.ok(transactionFacade.getAllTransactions());
    }
    //d6.1 LISTO

    //d6.2 LISTO
    //OBTENER UNA TRANSACCION POR ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionFacade.getTransactionById(id));
    }
    //d6.2 LISTO

    //d6.3 LISTO
    //CREAR UNA NUEVA TRANSACCION
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransferRequestDTO transferRequestDTO){
        TransactionDTO transactionDTO = transactionFacade.transferMoney(transferRequestDTO);
        return ResponseEntity.ok(transactionDTO);

    }
    //d6.3 LISTO

    //d6.5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        transactionFacade.deleteTransactionById(id);
        return ResponseEntity.noContent().build();
    }

    //d6.5


    //Get all transactions for account number
    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(transactionFacade.getTransactionsForAccount(accountNumber));
    }
    //Create New transaction
//    @PostMapping
//    public ResponseEntity<TransactionDTO> transferMoney(@RequestBody TransactionDTO transactionDTO){
//        return ResponseEntity.ok(transactionFacade.transferMoney(transactionDTO));
//    }
}
