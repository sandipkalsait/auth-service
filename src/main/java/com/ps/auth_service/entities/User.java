package com.ps.auth_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user_master")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String role;

    // Constructor with role (if provided)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = (role == null || role.isEmpty()) ? "USER" : role;  // Default to "USER" if role is null or empty
    }

    // Constructor without role (defaults to "USER")
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
