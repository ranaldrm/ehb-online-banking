package com.ehb.banking.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ehb.banking.dto.AccountResponse;
import com.ehb.banking.service.AccountService;

@Controller
public class UIController {

    private final AccountService accountService;


    public UIController (AccountService accountService){
        this.accountService = accountService;
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


    
}