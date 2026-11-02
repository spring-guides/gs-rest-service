package com.example.restservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class GreetingController {
    @GetMapping("/api/greeting")
    public String greeting() {
        return " Welcome to Banking Service!";
    }
}
