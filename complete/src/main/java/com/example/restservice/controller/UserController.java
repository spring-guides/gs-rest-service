package com.example.restservice.controller;

import com.example.restservice.entity.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/increaseAndGetAge")
    public int getAgeByUserId(int userId) {
        userMapper.increaseAge(userId);
        return userMapper.getAgeByUserIdForUpdate(userId);
    }
}
