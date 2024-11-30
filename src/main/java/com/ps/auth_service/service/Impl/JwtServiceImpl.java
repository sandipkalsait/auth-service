package com.ps.auth_service.service.Impl;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ps.auth_service.service.JwtService;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private String SECRET_KEY; // Dynamically generated key

    @Value("${jwt.secret.expiration}")
    private long EXP; // Expiration time from properties (in seconds)

    public JwtServiceImpl() {
        // Generate the secret key when the service is initialized
        this.SECRET_KEY = this.generateKey();
    }

    // Method to generate a secret key for HMACSHA256
    public String generateKey() {
        try {
            // Create a KeyGenerator instance for HmacSHA256
            log.info("Generating HMACSHA256 secret key...");
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

            // Generate the secret key
            SecretKey secretKey = keyGen.generateKey();

            // Return the Base64 encoded string of the generated key
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            log.info("Successfully generated secret key.");
            return encodedKey;

        } catch (NoSuchAlgorithmException e) {
            // Log the exception and handle it as needed
            log.error("Error generating key: ", e);
            return "Error generating key: " + e.getMessage(); // Handle the error appropriately
        }
    }

    @Override
    public String generateToken(String username) {
        log.info("Generating JWT token for username: {}", username);

        Map<String, Object> claims = new HashMap<>();

        // Set the claims and return the generated JWT
        String jwtToken = Jwts.builder()
                .claims().add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * EXP))
                .and()
                .signWith(getKey())
                .compact();

        log.info("JWT token generated for user: {} : {}", username, jwtToken);
        return jwtToken;
    }

    // Get the HMAC key from the secret key
    private Key getKey() {
        log.debug("Decoding the secret key from Base64...");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decode the Base64 encoded secret key
        log.debug("Successfully decoded the secret key.");
        return Keys.hmacShaKeyFor(keyBytes); // Return the HMAC key
    }
   
    @Override
    public String extractUsername(String token) {
        log.info("Extracting username from token...");
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        log.info("Extracted username: {}", username);
        return username;
    }


    private Claims extractAllClaims(String token) {
        try {
            log.debug("Extracting claims from token...");
            return Jwts.parser()
                    .verifyWith(getSigningKey()) // Set the key used to validate the JWT signature
                    .build()
                    .parseSignedClaims(token) // Parse the JWT token
                    .getPayload(); // Extract and return the claims
        } catch (Exception e) {
            log.error("Error while extracting claims: {}", e.getMessage());
            throw new RuntimeException("Could not extract claims from the token", e);
        }
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        log.info("Validating token for username: {}", userDetails.getUsername());
        String username = extractUsername(token);
        boolean isValid = (userDetails.getUsername().equals(username) && !isJwtTokenExpired(token));
        log.info("Token validation result: {}", isValid);
        return isValid;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decode Base64-encoded key
        return Keys.hmacShaKeyFor(keyBytes); // Generate the HMAC key
    }

    private boolean isJwtTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration(); // Extract the expiration date
        boolean isExpired = expiration.before(new Date()); // Check if the token is expired (i.e., before the current time)
        log.info("Is token expired? {}", isExpired);
        return isExpired;
    }

}
