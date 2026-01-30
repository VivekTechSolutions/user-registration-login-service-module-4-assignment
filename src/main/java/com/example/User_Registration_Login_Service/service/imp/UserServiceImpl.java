package com.example.User_Registration_Login_Service.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {

        // Check for duplicates
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Create User entity 
        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());

        User savedUser = userRepository.save(user);

        // Return response DTO (no password)
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    @Override
    public UserResponse loginUser(UserLoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // password verification
        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserNotFoundException("Invalid credentials");
        }

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
