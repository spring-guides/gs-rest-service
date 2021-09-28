package com.example.restservice;
//Resource Controller

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}

//@GetMapping annotation ensures that HTTP GET requests to /greeting are mapped to the greeting() method
/**@RequestParam binds the value of the query string parameter name into the name parameter of the greeting() method.
 If the name parameter is absent in the request, the defaultValue of World is used.
 
 
 
 **/