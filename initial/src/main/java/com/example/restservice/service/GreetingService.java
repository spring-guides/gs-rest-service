package com.example.restservice.service;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

  public String greet(String name) {
    String effectiveName = (name == null || name.isBlank()) ? "World" : name;
    return "Hello, " + effectiveName + "!";
  }
}
