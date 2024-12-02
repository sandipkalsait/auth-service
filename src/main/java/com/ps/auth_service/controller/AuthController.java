package com.ps.auth_service.controller;

import org.springframework.web.bind.annotation.RestController;
import com.ps.auth_service.entities.User;
import com.ps.auth_service.service.AuthService;
import com.ps.auth_service.service.JwtService;
import com.ps.auth_service.util.AuthRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("v1")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    JwtService jwtService;

    
    // Endpoint for registering a user
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody AuthRequest authRequest) {
        User user=new User(authRequest.getUsername(),authRequest.getPassword(),authRequest.getRole().orElse("USER"));
        log.info("Received request to register user: {}", user);
        try {
            log.info("Registering user: {}", user);
            authService.registerUser(user);
            log.info("User successfully registered: {}", user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully: " + user);
        } catch (Exception e) {
            log.error("Error occurred during user registration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthRequest authRequest) {
        User user=new User(authRequest.getUsername(),authRequest.getPassword());
        log.info("Received login request for user: {}", user);
        try {
            boolean isAuthenticated = authService.verifyUser(user);
            
            if (isAuthenticated) {
                log.info("User authenticated successfully: {}", user.getUsername());
                String authToken = jwtService.generateToken(user.getUsername());
                log.info("Generated JWT token for user: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully: " + authToken);
            } else {
                log.warn("Invalid credentials for user: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
            }
        } catch (Exception e) {
            log.error("Error occurred during user login: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
