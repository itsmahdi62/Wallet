package com.example.wallet.api;

import com.example.wallet.entity.Person;
import com.example.wallet.entity.Transaction;
import com.example.wallet.security.JwtHelper;
import com.example.wallet.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionAPI.class)
public class TransactionApiTest {
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private TransactionAPI transactionAPI;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and set up MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionAPI).build();
    }
    private Transaction createTransaction(Long id, Long amount, boolean isDeposit) {
        return Transaction.builder()
                .id(id)
                .createdTime(LocalTime.now())
                .createdDate(LocalDate.now())
                .transactionAmount(amount)
                .isDeposit(isDeposit)
                .accountBalanceAfterTransaction(amount) // Set balance accordingly
                .deletedDate(null)
                .build();
    }

    private String generateJwtToken() {
        // Example: Generate JWT token for a mock user
        Person mockPerson = new Person();
        mockPerson.setNationalId("3920841271"); // Replace with a valid nationalId
        return jwtHelper.generateToken(mockPerson);
    }

    @Test
    void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction(1L, 200000L, true));
        transactions.add(createTransaction(2L, 150000L, false));

        when(transactionService.findAllTransactions()).thenReturn(transactions);
        // Generate a JWT token for authorization
        String token = generateJwtToken();

        mockMvc.perform(get("/api/v1/transaction/getAll").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetAllTransactionsNoContent() throws Exception {
        when(transactionService.findAllTransactions()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/transaction/getAll"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateTransaction() throws Exception {
        Transaction transaction = createTransaction(null, 200000L, true);

        doNothing().when(transactionService).createTransaction(any(Transaction.class));

        mockMvc.perform(post("/api/v1/transaction/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Transaction saved !"));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        Long transactionId = 1L;

        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/api/v1/transaction/deleteTransaction/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction deleted"));
    }

    @Test
    void testGetAllDepositTransactions() throws Exception {
        List<Transaction> depositTransactions = new ArrayList<>();
        depositTransactions.add(createTransaction(1L, 200000L, true));

        when(transactionService.findAllDepositTransactions()).thenReturn(depositTransactions);

        mockMvc.perform(get("/api/v1/transaction/getAllDepositTransactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllDepositTransactionsNoContent() throws Exception {
        when(transactionService.findAllDepositTransactions()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/transaction/getAllDepositTransactions"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllWithdrawTransactions() throws Exception {
        List<Transaction> withdrawTransactions = new ArrayList<>();
        withdrawTransactions.add(createTransaction(1L, 150000L, false));

        when(transactionService.findAllWithdrawTransactions()).thenReturn(withdrawTransactions);

        mockMvc.perform(get("/api/v1/transaction/getAllWithdrawTransactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllWithdrawTransactionsNoContent() throws Exception {
        when(transactionService.findAllWithdrawTransactions()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/transaction/getAllWithdrawTransactions"))
                .andExpect(status().isNoContent());
    }
}
