package com.example.restservice;

import com.example.restservice.dto.AccountValidationRequest;
import com.example.restservice.service.AccountValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountValidationController {

    private final AccountValidationService accountValidationService;

    public AccountValidationController(
            AccountValidationService accountValidationService) {
        this.accountValidationService = accountValidationService;
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateAccount(
            @RequestBody AccountValidationRequest request) {

        String result = accountValidationService.validateAccount(request);

        if (result.startsWith("Invalid")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}
