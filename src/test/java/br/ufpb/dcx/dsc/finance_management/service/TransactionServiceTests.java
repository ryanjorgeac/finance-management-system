package br.ufpb.dcx.dsc.finance_management.service;

import br.ufpb.dcx.dsc.finance_management.DTOs.transaction.TransactionDTO;
import br.ufpb.dcx.dsc.finance_management.exceptions.InsufficientBalanceException;
import br.ufpb.dcx.dsc.finance_management.exceptions.ItemNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.Transaction;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.CategoryRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.TransactionRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import br.ufpb.dcx.dsc.finance_management.services.TransactionService;
import br.ufpb.dcx.dsc.finance_management.types.TransactionTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private Category category;
    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.valueOf(1000));

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setValue(BigDecimal.valueOf(500));
        transaction.setType(TransactionTypes.INCOMING);
        transaction.setUser(user);
        transaction.setCategory(category);

        transactionDTO = new TransactionDTO();
        transactionDTO.setUserId(1L);
        transactionDTO.setCategoryId(1L);
        transactionDTO.setValue(BigDecimal.valueOf(500));
        transactionDTO.setType("INCOMING");
    }

    @Test
    void testCreateTransactionSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(any(Transaction.class), eq(TransactionDTO.class))).thenReturn(transactionDTO);

        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(createdTransaction);
        assertEquals("INCOMING", createdTransaction.getType());
        assertEquals(BigDecimal.valueOf(500), createdTransaction.getValue());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransactionUserOrCategoryNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        assertEquals("User or Category not found. Try a valid value.", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransactionInsufficientBalance() {
        transactionDTO.setType("OUTCOMING");
        transactionDTO.setValue(BigDecimal.valueOf(2000));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        assertEquals("Outcoming transaction value must be less than or equals to user balance.", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionsByUserIdSuccess() {
        when(transactionRepository.findByUserId(anyLong())).thenReturn(List.of(transaction));
        when(modelMapper.map(any(Transaction.class), eq(TransactionDTO.class))).thenReturn(transactionDTO);

        List<TransactionDTO> transactions = transactionService.getTransactions(1L);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(BigDecimal.valueOf(500), transactions.get(0).getValue());
        verify(transactionRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetTransactionByIdSuccess() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        when(modelMapper.map(any(Transaction.class), eq(TransactionDTO.class))).thenReturn(transactionDTO);

        TransactionDTO foundTransaction = transactionService.getTransactionById(1L);

        assertNotNull(foundTransaction);
        assertEquals(BigDecimal.valueOf(500), foundTransaction.getValue());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTransactionByIdNotFound() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            transactionService.getTransactionById(1L);
        });

        assertEquals("Transaction 1 not found.", exception.getMessage());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTransactionSuccess() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(any(Transaction.class), eq(TransactionDTO.class))).thenReturn(transactionDTO);

        TransactionDTO updatedTransaction = transactionService.updateTransaction(1L, transactionDTO);

        assertNotNull(updatedTransaction);
        assertEquals(BigDecimal.valueOf(500), updatedTransaction.getValue());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testDeleteTransactionSuccess() {
        doNothing().when(transactionRepository).deleteById(anyLong());

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }
}
