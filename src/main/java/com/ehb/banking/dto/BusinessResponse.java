package com.ehb.banking.dto;

import java.util.List;

import com.ehb.banking.Business;

public record BusinessResponse (
    String businessID,
    String businessName,
    List<AccountResponse> accounts
){
    public static BusinessResponse from(Business business) {
        return new BusinessResponse(
            business.getBusinessID(), 
            business.getBusinessName(), 
            business.getAllAccounts().values().stream()
                                                .map(AccountResponse::from)
                                                .toList()
        );
    }

}