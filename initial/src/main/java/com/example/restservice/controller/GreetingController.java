package com.example.restservice.controller;

import com.example.restservice.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private final GreetingService greetingService;

  public GreetingController(GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  @GetMapping("/greeting")
  public GreetingResponse greeting(@RequestParam(name = "name", required = false) String name) {
    return new GreetingResponse(greetingService.greet(name));
  }
}
