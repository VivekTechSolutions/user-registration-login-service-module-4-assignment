package com.example.User_Registration_Login_Service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.User_Registration_Login_Service.dto.request.UserLoginRequest;
import com.example.User_Registration_Login_Service.dto.request.UserRegistrationRequest;
import com.example.User_Registration_Login_Service.dto.response.UserResponse;
import com.example.User_Registration_Login_Service.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // -------------------------
    // Register user
    // -------------------------
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        // INFO: Log incoming registration request (without sensitive data)
        logger.info("Incoming registration request for username: {}", request.getUsername());

        UserResponse response = userService.registerUser(request);

        // INFO: Log successful registration
        logger.info("Registration successful for userId: {}", response.getId());

        return ResponseEntity.status(201).body(response);
    }

    // -------------------------
    // Login user
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(
            @Valid @RequestBody UserLoginRequest request) {

        // INFO: Log incoming login attempt
        logger.info("Incoming login request for username: {}", request.getUsername());

        UserResponse response = userService.loginUser(request);

        // INFO: Log successful login
        logger.info("Login successful for userId: {}", response.getId());

        return ResponseEntity.ok(response);
    }
}
