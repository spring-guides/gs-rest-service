package com.example.restservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    public record Greeting(long id, String content) { }
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(required = false) String nome) {
        String nomeAux = (nome != null) ? nome : "World";
        return new Greeting(counter.incrementAndGet(),"Hello, "+nomeAux+"!");
    }

}
