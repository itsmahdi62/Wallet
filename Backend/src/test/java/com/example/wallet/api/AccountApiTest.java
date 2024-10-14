//package com.example.wallet.api;
//
//import com.example.wallet.entity.Account;
//import com.example.wallet.entity.Person;
//import com.example.wallet.security.JwtHelper;
//import com.example.wallet.service.AccountService;
//import com.example.wallet.service.PersonService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.hamcrest.CoreMatchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AccountAPI.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class AccountApiTest {
//
//    @MockBean
//    private AccountService accountService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private JwtHelper jwtHelper ;
//
//    @MockBean
//    private PersonService personService;
//    private Account account;
//
//    @BeforeEach
//    void setUp() {
//        account = Account.builder()
//                .id(1L)
//                .accountNumber("10000000000001")
//                .shaba("IR000000000000000000000001")
//                .creationDate(LocalDate.now())
//                .accountBalance(2000L)
//                .build();
//    }
//
//    @Test
//    void testGetAllAccounts() throws Exception {
//
//        List<Account> accounts = Arrays.asList(account);
//
//        when(accountService.findAllAccounts()).thenReturn(accounts);
//
//        String token = "Bearer " +
//                jwtHelper.generateToken(accountDetailsRepository.save(accountDetails));
//
//
//        mockMvc.perform(get("/api/v1/account/getAllAccounts")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].accountNumber").value("10000000000001"))
//                .andExpect(jsonPath("$[0].shaba").value("IR000000000000000000000001"))
//                .andExpect(jsonPath("$[0].accountBalance").value(2000L));
//
//        verify(accountService, times(1)).findAllAccounts();
//        given(accountService.createAccount(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
//    }
//
//    @Test
//    void testCreateAccount() throws Exception {
//        Person person = Person.builder().id(1L).name("John").build();
//        when(accountService.createAccount(any(Person.class))).thenReturn(account);
//
//        mockMvc.perform(post("/api/v1/account/createAccount")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(person)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountNumber").value("10000000000001"))
//                .andExpect(jsonPath("$.shaba").value("IR000000000000000000000001"))
//                .andExpect(jsonPath("$.accountBalance").value(2000L));
//
//        verify(accountService, times(1)).createAccount(any(Person.class));
//    }
//
//    @Test
//    void testUpdateAccount() throws Exception {
//        Account updatedAccount = Account.builder()
//                .id(1L)
//                .accountNumber("10000000000001")
//                .shaba("IR000000000000000000000001")
//                .creationDate(LocalDate.now())
//                .accountBalance(5000L) // Updated balance
//                .build();
//
//        when(accountService.updateAccountInfo(anyLong(), any(Account.class))).thenReturn(updatedAccount);
//
//        mockMvc.perform(post("/api/v1/account/update-account-info/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedAccount)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountBalance").value(5000L));
//
//        verify(accountService, times(1)).updateAccountInfo(anyLong(), any(Account.class));
//    }
//
//    @Test
//    void testDeleteAccount() throws Exception {
//        doNothing().when(accountService).deleteAccount(anyLong());
//
//        mockMvc.perform(post("/api/v1/account/delete-account/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Account deleted !"));
//
//        verify(accountService, times(1)).deleteAccount(anyLong());
//    }
//}
