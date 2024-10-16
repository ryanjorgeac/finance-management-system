package br.ufpb.dcx.dsc.finance_management.services;

import br.ufpb.dcx.dsc.finance_management.DTOs.CategoryDTO;
import br.ufpb.dcx.dsc.finance_management.DTOs.TransactionDTO;
import br.ufpb.dcx.dsc.finance_management.exceptions.ItemNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.Transaction;
import br.ufpb.dcx.dsc.finance_management.repositories.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    private TransactionDTO convertToDTO(Transaction transaction){
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO){
        return modelMapper.map(transactionDTO, Transaction.class);
    }


    public List<TransactionDTO> getTransactions(Long userId) {
        List<Transaction> transactions;
        if(userId == null){
            transactions = transactionRepository.findAll();
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }
        return  transactions
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TransactionDTO getTransactionById(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            return convertToDTO(transactionOptional.get());
        } else {
            throw new ItemNotFoundException("Transaction " + transactionId + " not found.");
        }
    }


}
