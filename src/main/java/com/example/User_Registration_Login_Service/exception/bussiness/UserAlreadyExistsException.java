package com.example.User_Registration_Login_Service.exception.bussiness;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}