package br.ufpb.dcx.dsc.finance_management.services;

import br.ufpb.dcx.dsc.finance_management.DTOs.TransactionDTO;
import br.ufpb.dcx.dsc.finance_management.exceptions.ItemNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.Transaction;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.CategoryRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.TransactionRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Optional<User> user = userRepository.findById(transactionDTO.getUserId());
        Optional<Category> category = categoryRepository.findById(transactionDTO.getCategoryId());
        if (user.isPresent() && category.isPresent()) {
            User toUpdate = user.get();
            Category category1 = category.get();
            Transaction transaction = new Transaction();

            transaction.setDescription(transactionDTO.getDescription());
            transaction.setDate(transactionDTO.getDate());
            transaction.setValue(transactionDTO.getValue());
            transaction.setUser(toUpdate);
            transaction.setCategory(category1);
            transaction.setType(transactionDTO.getType());

            Transaction created = transactionRepository.save(transaction);
            return convertToDTO(created);
        } else {
            throw new ItemNotFoundException("User or Category not found. Try a valid value.");
        }
    }

}
