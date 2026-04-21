package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private static final String template = "Hello, %s! Welcome to our REST API.";

  private static final String farewellTemplate = "Goodbye, %s!";

  // Added congratulations template for the new /congratulations endpoint
  private static final String congratsTemplate = "Congratulations, %s!";
  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/greeting")
  public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), template.formatted(name));
  }

  @GetMapping("/farewell")
  public Greeting farewell(@RequestParam(defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), farewellTemplate.formatted(name));
  }

  // Added new /congratulations endpoint that returns a congratulations message
  // Accepts an optional 'name' parameter, defaults to "World" if not provided
  @GetMapping("/congratulations")
  public Greeting congratulations(@RequestParam(defaultValue = "World") String name) {
      return new Greeting(counter.incrementAndGet(), congratsTemplate.formatted(name));
  }
}