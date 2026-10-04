package com.ehb.banking.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ehb.banking.domain.Account;
import com.ehb.banking.domain.Business;
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

    public List<Business> getAllBusinesses(){
        return businessRepository.findAll();
    }

}