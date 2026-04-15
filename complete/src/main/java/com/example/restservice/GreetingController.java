package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private static final String template = "Hello, %s!";

  // Added farewell template for the new /farewell endpoint
  private static final String farewellTemplate = "Goodbye, %s!";
  
  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/greeting")
  public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), template.formatted(name));
  }

  // Added new /farewell endpoint that returns a goodbye message
  // Accepts an optional 'name' parameter, defaults to "World" if not provided
  @GetMapping("/farewell")
  public Greeting farewell(@RequestParam(defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), farewellTemplate.formatted(name));
  }

}