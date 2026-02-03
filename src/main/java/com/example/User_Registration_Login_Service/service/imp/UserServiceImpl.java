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

    // Logger used to track application flow and important events without logging sensitive data
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {

        // Logs user registration attempt for traceability
        logger.info("User registration attempt for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Registration failed: Username already exists [{}]", request.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration failed: Email already exists [{}]", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        // Logs successful registration event with generated user ID
        logger.info("User registered successfully with userId: {}", savedUser.getId());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    @Override
    public UserResponse loginUser(UserLoginRequest request) {

        // Logs login attempt using username (password is never logged)
        logger.info("Login attempt for username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User not found [{}]", request.getUsername());
                    return new UserNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid credentials for username [{}]", request.getUsername());
            throw new UserNotFoundException("Invalid credentials");
        }

        logger.info("Login successful for userId: {}", user.getId());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
