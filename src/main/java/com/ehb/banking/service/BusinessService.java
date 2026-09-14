package com.ehb.banking.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ehb.banking.Account;
import com.ehb.banking.Business;
import com.ehb.banking.exceptions.BusinessNotFoundException;
import com.ehb.banking.repository.BusinessRepository;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;

    public BusinessService (BusinessRepository businessRepository){
        this.businessRepository = businessRepository;
    }


    public Business getBusinessByID (String id){
        return businessRepository.getBusinessByID(id).orElseThrow(() -> new BusinessNotFoundException("Business: " + id + " not found"));
    }

    public Map<String,Account> getAccountsForBusiness(String id) {
        return getBusinessByID(id).getAllAccounts();
        
    }

}