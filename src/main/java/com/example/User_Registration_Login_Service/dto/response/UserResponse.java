package com.example.User_Registration_Login_Service.dto.response;

public class UserResponse {

    private Long id;
    private String username;
    private String email;

    // Constructor
    public UserResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters only (no password)
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}