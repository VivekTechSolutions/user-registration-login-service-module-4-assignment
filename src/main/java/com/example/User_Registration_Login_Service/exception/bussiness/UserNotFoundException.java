package com.example.User_Registration_Login_Service.exception.bussiness;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
