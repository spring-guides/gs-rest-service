package com.example.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.*;

@SpringBootApplication
public class RestServiceApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);
    }

}
