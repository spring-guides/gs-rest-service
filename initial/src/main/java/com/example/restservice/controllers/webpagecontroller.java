package com.example.restservice.controllers;

import com.example.restservice.services.Firstservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class webpagecontroller {

    private Firstservice firstservice;

    public webpagecontroller(Firstservice firstservice){
        this.firstservice=firstservice;
    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    //	@GetMapping("/greeting")
    @RequestMapping(value = "/greeting/get",method = RequestMethod.GET)
    public String greeting(@RequestParam(value = "name", defaultValue = "User!") String name) {
        return firstservice.greet(counter.incrementAndGet(),String.format(template, name));

    }
    @RequestMapping(value = "/greeting/post",headers = "key=val",method = RequestMethod.POST)
    @ResponseBody
    public String greetingpost(@RequestParam(value = "name", defaultValue = "User!") String name) {
        return "New POSt";
    }

}
