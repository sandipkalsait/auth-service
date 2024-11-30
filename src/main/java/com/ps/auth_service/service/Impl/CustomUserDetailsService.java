package com.ps.auth_service.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ps.auth_service.entities.User;
import com.ps.auth_service.entities.UserPrincipal;
import com.ps.auth_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads user details by username from the database.
     * 
     * @param username The username of the user to load.
     * @return UserDetails for authentication and authorization.
     * @throws UsernameNotFoundException if the username does not exist.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Username '{}' not found in records", username);
            throw new UsernameNotFoundException("Username not found in records!");
        }

        log.info("Successfully loaded user: {}", username);
        return new UserPrincipal(user);
    }
}
