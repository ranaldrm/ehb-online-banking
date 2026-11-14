package com.ehb.banking.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ehb.banking.dto.AccountResponse;
import com.ehb.banking.dto.BusinessResponse;
import com.ehb.banking.dto.TransactionResponse;
import com.ehb.banking.service.AccountService;
import com.ehb.banking.service.BusinessService;

@Controller
public class UIController {

    private final AccountService accountService;
    private final BusinessService businessService;


    public UIController (AccountService accountService, BusinessService businessService){
        this.accountService = accountService;
        this.businessService = businessService;
    }

    
    @GetMapping("/")
    public String bankHome(Model model) {
        model.addAttribute("pageStatus", "EHB is running");
        List<AccountResponse> accounts = accountService.getAllAccounts()
            .stream()
            .map(AccountResponse::from)
            .toList();
        model.addAttribute("accounts", accounts);
        return "bankhome";
    }



    @GetMapping("/businesses")
    public String businesses(Model model){
        List<BusinessResponse> businesses = businessService.getAllBusinesses()
            .stream()
            .map(BusinessResponse::from)
            .toList();
        model.addAttribute("businesses", businesses);
        return "businesses";
    }


    @GetMapping("/businesses/{businessID}")
    public String businessDetails(@PathVariable String businessID, Model model) {
        BusinessResponse business = BusinessResponse.from(businessService.getBusinessByID(businessID));
        List<AccountResponse> accounts = business.accounts();
        model.addAttribute("business", business);
        model.addAttribute("accounts", accounts);
        return "business-details";
        


    }




    @GetMapping("/accounts/{accountNumber}")
    public String accountDetails(@PathVariable String accountNumber ,Model model) {
        AccountResponse account = AccountResponse.from(accountService.getAccountByNumber(accountNumber));
        List<TransactionResponse> transactions = accountService.getTransactionHistory(accountNumber)
            .stream()
            .map(TransactionResponse::from)
            .toList();
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        return "account-details";

    }


    
}