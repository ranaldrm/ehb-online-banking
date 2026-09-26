package com.ehb.banking;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ehb.banking.controller.PaymentController;
import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.service.AccountService;
import com.ehb.banking.service.PaymentService;


@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    // PaymentService depends on AccountService; WebMvcTest will fail to load
    // the context without this even though it is not used directly in tests.
    @MockBean
    private AccountService accountService;


    @Test
    void postPayment_validRequest_returns200() throws Exception {
        Payment payment = new Payment(
                new BigDecimal("250.00"), "ACC001", "ACC002", Currency.GBP);
        payment.validate();
        payment.approve();
        payment.complete();

        Mockito.when(paymentService.processPayment("ACC001", "ACC002", new BigDecimal("250.00")))
               .thenReturn(payment);

        mockMvc.perform(post("/api/payments")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("""
                               {
                                 "sourceAccountNumber": "ACC001",
                                 "targetAccountNumber": "ACC002",
                                 "paymentAmount": 250.00
                               }
                               """))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.sourceAccountNumber").value("ACC001"))
               .andExpect(jsonPath("$.targetAccountNumber").value("ACC002"))
               .andExpect(jsonPath("$.paymentStatus").value("COMPLETED"));
    }

    @Test
    void postPayment_missingField_returns400() throws Exception {
        // sourceAccountNumber is blank — @NotBlank on PaymentRequest will reject it
        mockMvc.perform(post("/api/payments")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("""
                               {
                                 "sourceAccountNumber": "",
                                 "targetAccountNumber": "ACC002",
                                 "paymentAmount": 250.00
                               }
                               """))
               .andExpect(status().isBadRequest());
    }

    @Test
    void postPayment_insufficientFunds_returns409() throws Exception {
        Mockito.when(paymentService.processPayment("ACC001", "ACC002", new BigDecimal("9999.00")))
               .thenThrow(new ExceedsBalanceException("Payment exceeds balance"));

        mockMvc.perform(post("/api/payments")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("""
                               {
                                 "sourceAccountNumber": "ACC001",
                                 "targetAccountNumber": "ACC002",
                                 "paymentAmount": 9999.00
                               }
                               """))
               .andExpect(status().isConflict());
    }
}
