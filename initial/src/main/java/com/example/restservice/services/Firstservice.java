package com.example.restservice.services;

import org.springframework.stereotype.Service;

@Service
public class Firstservice {

    public record Greeting(long id, String content) {

    }
    public String greet(long id,String string){
        return "id "+id+" My Name is "+string;
    }
}
