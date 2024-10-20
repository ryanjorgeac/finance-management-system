package br.ufpb.dcx.dsc.finance_management.services;

import br.ufpb.dcx.dsc.finance_management.DTOs.transaction.TransactionDTO;
import br.ufpb.dcx.dsc.finance_management.exceptions.InsufficientBalanceException;
import br.ufpb.dcx.dsc.finance_management.exceptions.ItemNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.Transaction;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.CategoryRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.TransactionRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import br.ufpb.dcx.dsc.finance_management.types.TransactionTypes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    private BigDecimal getUserBalance(Long userId) {
        List<Transaction> userTransactions = transactionRepository.findByUserId(userId);
        return userTransactions.stream()
                .map(t -> t.getValue().multiply(BigDecimal.valueOf(t.getType().getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
        if (transactionDTO.getCategoryId() == null) {
            throw new ItemNotFoundException("Category ID is required and cannot be null.");
        }

        Optional<User> user = userRepository.findById(transactionDTO.getUserId());
        Optional<Category> category = categoryRepository.findById(transactionDTO.getCategoryId());

        if (user.isPresent() && category.isPresent()) {
            User toUpdate = user.get();
            Category category1 = category.get();
            Transaction transaction = new Transaction();
            if (transactionDTO.getType() == TransactionTypes.OUTCOMING) {
                BigDecimal currentBalance = getUserBalance(transactionDTO.getUserId());
                if (currentBalance.compareTo(transactionDTO.getValue()) < 0) {
                    throw new InsufficientBalanceException("Insufficient balance to complete this OUTCOMING transaction.");
                }
            }
            transaction.setDescription(transactionDTO.getDescription());
            transaction.setDate(transactionDTO.getDate());
            transaction.setValue(transactionDTO.getValue());
            transaction.setUser(toUpdate);
            transaction.setCategory(category1);
            transaction.setType(transactionDTO.getType());

            Transaction transaction = getTransaction(transactionDTO, category1, toUpdate);
            Transaction created = transactionRepository.save(transaction);
            BigDecimal valueToAdd = created.getValue().multiply(BigDecimal.valueOf(created.getType().getValue()));
            toUpdate.addToBalance(valueToAdd);
            userRepository.save(toUpdate);
            return convertToDTO(created);
        } else {
            throw new ItemNotFoundException("User or Category not found. Try a valid value.");
        }
    }

    private static Transaction getTransaction(TransactionDTO transactionDTO, Category category, User toUpdate) {
        BigDecimal value = transactionDTO.getValue();
        TransactionTypes type = transactionDTO.getType();
        Transaction transaction = new Transaction();
        if (type == TransactionTypes.OUTCOMING && value.compareTo(toUpdate.getBalance()) > 0) {
            throw new InvalidTransactionValue("Outcoming transaction value must be less than or equals to user balance.");
        }

        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(transactionDTO.getDate());
        transaction.setValue(value);
        transaction.setUser(toUpdate);
        transaction.setCategory(category);
        transaction.setType(type);
        return transaction;
    }

}
