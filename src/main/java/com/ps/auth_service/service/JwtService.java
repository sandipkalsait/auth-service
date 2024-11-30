package com.ps.auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
 
    public String generateToken(String username);

    public boolean validateToken(String token, UserDetails userDetails);

    public String extractUsername(String token);

}
