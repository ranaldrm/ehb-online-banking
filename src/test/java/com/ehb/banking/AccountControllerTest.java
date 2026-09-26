package com.ehb.banking;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ehb.banking.controller.AccountController;
import com.ehb.banking.exceptions.AccountNotFoundException;
import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.service.AccountService;


@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;


    @Test
    void getAccount_existingAccount_returns200() throws Exception {
        Account account = new Account("ACC001", Currency.GBP);
        Mockito.when(accountService.getAccountByNumber("ACC001")).thenReturn(account);

        mockMvc.perform(get("/api/accounts/ACC001"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accountNumber").value("ACC001"))
               .andExpect(jsonPath("$.currency").value("GBP"))
               .andExpect(jsonPath("$.balance").value(0));
    }

    @Test
    void getAccount_unknownAccount_returns404() throws Exception {
        Mockito.when(accountService.getAccountByNumber("UNKNOWN"))
               .thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/UNKNOWN"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deposit_validAmount_returns200WithTransactionResponse() throws Exception {
        Transaction transaction = Transaction.of(TransactionType.INCOMING, new BigDecimal("500.00"));
        Mockito.when(accountService.deposit("ACC001", new BigDecimal("500.00")))
               .thenReturn(transaction);

        mockMvc.perform(post("/api/accounts/ACC001/deposit")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"depositAmount\": 500.00}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.transactionType").value("INCOMING"))
               .andExpect(jsonPath("$.transactionAmount").value(500.00));
    }

    @Test
    void deposit_zeroAmount_returns400() throws Exception {
        mockMvc.perform(post("/api/accounts/ACC001/deposit")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"depositAmount\": 0}"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_insufficientFunds_returns409() throws Exception {
        Mockito.when(accountService.withdraw("ACC001", new BigDecimal("9999.00")))
               .thenThrow(new ExceedsBalanceException("Withdrawal exceeds balance"));

        mockMvc.perform(post("/api/accounts/ACC001/withdraw")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"withdrawAmount\": 9999.00}"))
               .andExpect(status().isConflict());
    }
}