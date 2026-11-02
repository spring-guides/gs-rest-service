package com.example.restservice.service;

import com.example.restservice.dto.AccountValidationRequest;
import com.example.restservice.exception.InvalidAccountException;
import org.springframework.stereotype.Service;

@Service
public class AccountValidationService {

    public String validateAccount(AccountValidationRequest request){

        if (request.getAccountNumber() == null ||
                request.getAccountNumber().isEmpty()) {

            throw new InvalidAccountException("Account number is required");
        }

        if (request.getIfscCode() == null || request.getIfscCode().isEmpty()) {

            throw new InvalidAccountException("IFSC code is required");
        }
        return  "Account Details are valid";

    }

}
