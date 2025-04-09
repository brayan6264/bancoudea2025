package com.udea.bancoudea.service;

import com.udea.bancoudea.DTO.TransactionDTO;
import com.udea.bancoudea.DTO.TransferRequestDTO;
import com.udea.bancoudea.entity.Customer;
import com.udea.bancoudea.entity.Transaction;
import com.udea.bancoudea.mapper.TransactionMapper;
import com.udea.bancoudea.repository.CustomerRepository;
import com.udea.bancoudea.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

//    @Autowired
    private final TransactionRepository transactionRepository;

//    @Autowired
    private final CustomerRepository customerRepository;

    //d
//    @Autowired
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.transactionMapper = transactionMapper;
    }


    //OBTENER TODAS LAS TRANSACCIONES
    public List<TransactionDTO> getAllTransactions(){
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO).toList();
    }
    //d
    //d6.2
    //OBTENER TRANSACCION POR ID
    public TransactionDTO getTransactionById(Long id){
        return transactionRepository.findById(id).map(transactionMapper::toDTO)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada con ID: " + id));
//                .orElseThrow(()->new RuntimeException("Id de la transferencia no encontrado"));
    }
    //d6.2

    //d6.3


    //d Voy a modificar el parámetro de la función de transaction a requesttransaction
    public TransactionDTO transferMoney(TransferRequestDTO transferRequestDTO) {
        //validar que los numeros de cuenta no sean nulos
        if(transferRequestDTO.getSenderAccountNumber()==null || transferRequestDTO.getReceiverAccountNumber()==null){
            throw new IllegalArgumentException("Sender Account Number or Receiver Account Number cannot be null");
        }

        //Buscar los clientes por numero de cuenta
        Customer sender = customerRepository.findByAccountNumber(transferRequestDTO.getSenderAccountNumber())
                .orElseThrow(()-> new IllegalArgumentException("Sender Account Number not found"));

        Customer receiver = customerRepository.findByAccountNumber(transferRequestDTO.getReceiverAccountNumber())
                .orElseThrow(()-> new IllegalArgumentException("Receiver Account Number not found"));

        //Validar que el remitente tenga saldo suficiente
        if(sender.getBalance() < transferRequestDTO.getAmount()){
            throw new IllegalArgumentException("Sender Balance not enough");
        }

        //realiza la transferencia
        sender.setBalance(sender.getBalance() - transferRequestDTO.getAmount());
        receiver.setBalance(receiver.getBalance() + transferRequestDTO.getAmount());

        //Guardar los cambios en las cuentas
        customerRepository.save(sender);
        customerRepository.save(receiver);

        //Crear y guardar la transaccion
        Transaction transaction = new Transaction();
        transaction.setSenderAccountNumber(sender.getAccountNumber());
        transaction.setReceiverAccountNumber(receiver.getAccountNumber());
        transaction.setAmount(transferRequestDTO.getAmount());
        transaction= transactionRepository.save(transaction);

        //Devolver la transaccion creada como un DTO
        TransactionDTO  savedTransaction = new TransactionDTO();
        savedTransaction.setId(transaction.getId());
        savedTransaction.setSenderAccountNumber(transaction.getSenderAccountNumber());
        savedTransaction.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
        savedTransaction.setAmount(transaction.getAmount());
        return savedTransaction;

    }
    //d6.3


    public List<TransactionDTO> getTransactionsForAccount(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(accountNumber,accountNumber);
        return transactions.stream().map(transaction -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(transaction.getId());
            dto.setSenderAccountNumber(transaction.getSenderAccountNumber());
            dto.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
            dto.setAmount(transaction.getAmount());
            return dto;
        }).collect(Collectors.toList());
    }

    //d6.5
    public void deleteTransactionById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada con id: " + id));
        transactionRepository.delete(transaction);

    }
    //d6.5

}
