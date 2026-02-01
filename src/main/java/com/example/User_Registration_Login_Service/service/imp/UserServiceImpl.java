package com.example.User_Registration_Login_Service.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.User_Registration_Login_Service.dto.request.UserLoginRequest;
import com.example.User_Registration_Login_Service.dto.request.UserRegistrationRequest;
import com.example.User_Registration_Login_Service.dto.response.UserResponse;
import com.example.User_Registration_Login_Service.entity.User;
import com.example.User_Registration_Login_Service.exception.bussiness.UserAlreadyExistsException;
import com.example.User_Registration_Login_Service.exception.bussiness.UserNotFoundException;
import com.example.User_Registration_Login_Service.repository.UserRepository;
import com.example.User_Registration_Login_Service.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    // Logger for service-layer business flow (no sensitive data logged)
    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Handles user registration with validation and safe logging
    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {

    	// Log registration attempt with username
        logger.info("User registration attempt for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Registration failed: Username already exists [{}]", request.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration failed: Email already exists [{}]", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        User savedUser = userRepository.save(user);

        logger.info("User registered successfully with userId: {}", savedUser.getId());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    // Handles user login with credential validation and contextual logging
    @Override
    public UserResponse loginUser(UserLoginRequest request) {

        logger.info("Login attempt for username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User not found [{}]", request.getUsername());
                    return new UserNotFoundException("User not found");
                });

        if (!user.getPassword().equals(request.getPassword())) {
            logger.warn("Login failed: Invalid credentials for username [{}]", request.getUsername());
            throw new UserNotFoundException("Invalid credentials");
        }

     // Log login attempt using username
        logger.info("Login successful for userId: {}", user.getId());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
