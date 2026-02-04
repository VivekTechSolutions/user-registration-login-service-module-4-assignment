package com.example.User_Registration_Login_Service.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        // INFO: Normal application flow - user registration attempt
        logger.info("User registration attempt for username: {}", request.getUsername());

        // ERROR: Operation fails because username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            logger.error("Registration failed: Username already exists [{}]", request.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        // ERROR: Operation fails because email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.error("Registration failed: Email already exists [{}]", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Encode password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        // INFO: Registration successful
        logger.info("User registered successfully with userId: {}", savedUser.getId());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    @Override
    public UserResponse loginUser(UserLoginRequest request) {
        // INFO: Normal application flow - login attempt
        logger.info("Login attempt for username: {}", request.getUsername());

        // ERROR: User not found
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.error("Login failed: User not found [{}]", request.getUsername());
                    return new UserNotFoundException("User not found");
                });

        // WARN: Invalid credentials (user exists but password does not match)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid credentials for username [{}]", request.getUsername());
            throw new UserNotFoundException("Invalid credentials");
        }

        // INFO: Login successful
        logger.info("Login successful for userId: {}", user.getId());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
