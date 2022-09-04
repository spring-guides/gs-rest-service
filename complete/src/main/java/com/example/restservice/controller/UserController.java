package com.example.restservice.controller;

import com.example.restservice.entity.User;
import com.example.restservice.entity.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/increase")
    public int increaseAge(int userId) {
        userMapper.increaseAge(userId);
//        User user = userMapper.findUserById(userId);
//        return user;
        return userMapper.getAgeByUserIdForUpdate(userId);
    }
}
