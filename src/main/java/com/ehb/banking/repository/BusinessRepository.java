package com.ehb.banking.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ehb.banking.Business;




@Component
public class BusinessRepository {

    private static final Logger log = LoggerFactory.getLogger(BusinessRepository.class);

    private Map<String, Business> businesses;

    public BusinessRepository (AccountRepository accountRepository) {
        businesses = new LinkedHashMap<>();
        Business businessDodgyDave = new Business("Dodgy Dave's", "password1");
        log.info("Created business '{}' with ID: {}", businessDodgyDave.getBusinessName(), businessDodgyDave.getBusinessID());
        
        Business businessBrigadoonUniversity = new Business("University of Brigadoon", "password1");
        log.info("Created business '{}' with ID: {}", businessBrigadoonUniversity.getBusinessName(), businessBrigadoonUniversity.getBusinessID());
               
        Business businessWorkShyConsultants = new Business("workShy Consultants LLC", "password1");
        log.info("Created business '{}' with ID: {}", businessWorkShyConsultants.getBusinessName(), businessWorkShyConsultants.getBusinessID());


        businesses.put(businessDodgyDave.getBusinessID(), businessDodgyDave);
        businesses.put(businessBrigadoonUniversity.getBusinessID(), businessBrigadoonUniversity);
        businesses.put(businessWorkShyConsultants.getBusinessID(),businessWorkShyConsultants);

        businessDodgyDave.addAccount(accountRepository.findByAccountNumber("1111").orElseThrow());
        businessDodgyDave.addAccount(accountRepository.findByAccountNumber("2222").orElseThrow());
        businessBrigadoonUniversity.addAccount(accountRepository.findByAccountNumber("3333").orElseThrow());
        businessBrigadoonUniversity.addAccount(accountRepository.findByAccountNumber("4444").orElseThrow());
        businessWorkShyConsultants.addAccount(accountRepository.findByAccountNumber("5555").orElseThrow());      
    }

    public Map<String, Business> getBusinesses () {
        return Map.copyOf(businesses);
    }

    public Optional<Business> getBusinessByID (String id){
        return Optional.ofNullable(this.businesses.get(id));
    }

    
}