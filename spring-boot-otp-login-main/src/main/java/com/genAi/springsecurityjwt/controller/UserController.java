package com.genAi.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.genAi.springsecurityjwt.model.User;
import com.genAi.springsecurityjwt.service.UserDetailsService;

@RestController
@RequestMapping("/api/register")
public class UserController {

    @Autowired
    private UserDetailsService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Check if the email is already registered
//        if (userService.isEmailAlreadyRegistered(user.getUserName())) {
//            return ResponseEntity.badRequest().body("Email is already registered");
//        }

        // Register the user
//        User registeredUser = userService.registerUser(user);

        // You might generate a JWT token here if needed

        return ResponseEntity.ok("User registered successfully");
    }
}
