package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	//	@GetMapping("/greeting")
	@RequestMapping(value = "/greeting/get",method = RequestMethod.GET)
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "User!") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	@RequestMapping(value = "/greeting/post",headers = "key=val",method = RequestMethod.POST)
	@ResponseBody
	public String greetingpost(@RequestParam(value = "name", defaultValue = "User!") String name) {
		return "New POSt";
	}
}
