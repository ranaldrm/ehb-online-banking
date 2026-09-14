package com.ehb.banking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehb.banking.Business;
import com.ehb.banking.dto.AccountResponse;
import com.ehb.banking.dto.BusinessResponse;
import com.ehb.banking.service.BusinessService;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/{id}")
    public BusinessResponse getBusinessResponse (@PathVariable(value="id") String id){
        Business business = businessService.getBusinessByID(id);
        return BusinessResponse.from(business);
    }

    
    
    @GetMapping("/{id}/accounts")
    public List<AccountResponse> getAccountsForBusiness (@PathVariable(value="id") String id) {
        Business business = businessService.getBusinessByID(id);
        return BusinessResponse.from(business).accounts();
        
    }





}