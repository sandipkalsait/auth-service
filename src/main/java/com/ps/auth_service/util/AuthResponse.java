package com.ps.auth_service.util;

import java.util.Optional;

public class AuthResponse {

    private String message;  // Message describing the response (nullable)
    private boolean success; // Success flag (true or false)
    private Optional<Object> data; // Optional data that can hold any type of object or be empty

    // Constructor for response with success flag, message, and optional data
    public AuthResponse(String message, boolean success, Optional<Object> data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // Constructor for response with only success flag and message (data is empty)
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.data = Optional.empty(); // Default to empty Optional
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Optional<Object> getData() {
        return data;
    }

    public void setData(Optional<Object> data) {
        this.data = data;
    }
}
