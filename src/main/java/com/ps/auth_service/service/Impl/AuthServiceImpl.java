package com.ps.auth_service.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ps.auth_service.entities.User;
import com.ps.auth_service.repository.UserRepository;
import com.ps.auth_service.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Use PasswordEncoder for encoding passwords
    @Autowired
    AuthenticationProvider authenticationProvider;

    @Override
    public User registerUser(User user) {
        log.info("Attempting to register user with username: {}", user.getUsername());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            log.debug("Encoding provided password for user: {}", user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            log.warn("No password provided for user: {}, setting default password", user.getUsername());
            user.setPassword(passwordEncoder.encode("Admin@123")); // Set default password
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());

        return savedUser;
    }

    @Override
    public boolean verifyUser(User user) {
        try {
            Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            return authentication.isAuthenticated();  // Returns true if authenticated, false otherwise
        } catch (AuthenticationException e) {
            // Log exception and return false if authentication fails
            log.error("Authentication failed for user: {}", user.getUsername(), e);
            return false;
        }
    }
    
}
