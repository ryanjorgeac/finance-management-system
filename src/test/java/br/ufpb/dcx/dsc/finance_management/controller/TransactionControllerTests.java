package br.ufpb.dcx.dsc.finance_management.controller;

import br.ufpb.dcx.dsc.finance_management.DTOs.transaction.TransactionDTO;
import br.ufpb.dcx.dsc.finance_management.controllers.TransactionController;
import br.ufpb.dcx.dsc.finance_management.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        transactionDTO = new TransactionDTO();
        transactionDTO.setUserId(1L);
        transactionDTO.setCategoryId(1L);
        transactionDTO.setValue(BigDecimal.valueOf(500));
        transactionDTO.setType("INCOMING");
    }

    @Test
    void testGetTransactionsSuccess() throws Exception {
        List<TransactionDTO> transactionList = Arrays.asList(transactionDTO);

        when(transactionService.getTransactions(anyLong())).thenReturn(transactionList);

        mockMvc.perform(get("/api/transactions")
                        .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].value").value(500));
    }

    @Test
    void testGetTransactionByIdSuccess() throws Exception {
        when(transactionService.getTransactionById(anyLong())).thenReturn(transactionDTO);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.value").value(500));
    }

    @Test
    void testDeleteTransactionSuccess() throws Exception {
        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
