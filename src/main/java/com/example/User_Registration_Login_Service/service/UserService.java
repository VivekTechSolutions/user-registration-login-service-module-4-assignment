package com.example.User_Registration_Login_Service.service;

import com.example.User_Registration_Login_Service.dto.request.UserLoginRequest;
import com.example.User_Registration_Login_Service.dto.request.UserRegistrationRequest;
import com.example.User_Registration_Login_Service.dto.response.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRegistrationRequest request);

    UserResponse loginUser(UserLoginRequest request);
}