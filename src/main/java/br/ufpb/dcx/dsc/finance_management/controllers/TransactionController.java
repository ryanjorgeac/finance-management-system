package br.ufpb.dcx.dsc.finance_management.controllers;


import br.ufpb.dcx.dsc.finance_management.DTOs.TransactionDTO;
import br.ufpb.dcx.dsc.finance_management.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestParam(value = "user_id", required = false) Long userId) {
        List<TransactionDTO> transactions = transactionService.getTransactions(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO transaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

}
