/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dacelent
 */
@RestController
public class HealthController extends RuntimeException {

    private Boolean status = true;

    @GetMapping("/healthz")
    public ResponseEntity healthz() {

        if (status) {
            String body = "Running";
            System.out.println(body);
            return new ResponseEntity(body, HttpStatus.OK);
        } else {
            System.out.println("Not running");
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/switch")
    public void switchHealth() {
        status = !status;
    }

}
