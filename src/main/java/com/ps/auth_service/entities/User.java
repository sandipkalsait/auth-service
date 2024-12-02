package com.ps.auth_service.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user_master")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    private Role[] roles;  // Enum for user roles: ADMIN, USER, SUPERUSER

    private boolean isActive = true;  // New field to represent whether the user is active
    private boolean isLocked = false;  // New field to represent if the account is locked
    private boolean isCredentialsExpired = false;  // New field to represent if the credentials are expired
    private boolean isAccountExpired = false;  // New field to represent if the account is expired

    // Constructor with roles (if provided)
    public User(String username, String password, Role[] roles) {
        this.username = username;
        this.password = password;
        this.roles = (roles != null && roles.length > 0) ? roles : new Role[]{Role.USER};  // Default to "USER" if roles are null or empty
    }

    // Constructor without roles (defaults to "USER")
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new Role[]{Role.USER};  // Default role is "USER"
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities based on roles
        return Set.of(roles).stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())  // Prefix with "ROLE_" to match Spring Security conventions
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isAccountExpired;  // Check if the account is expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isLocked;  // Check if the account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialsExpired;  // Check if the credentials are expired
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;  // Check if the account is active
    }

    // Additional methods for managing account status
    public void lockAccount() {
        this.isLocked = true;
    }

    public void unlockAccount() {
        this.isLocked = false;
    }

    public void deactivateAccount() {
        this.isActive = false;
    }

    public void activateAccount() {
        this.isActive = true;
    }

    public void expireCredentials() {
        this.isCredentialsExpired = true;
    }

    public void unexpireCredentials() {
        this.isCredentialsExpired = false;
    }

    public void expireAccount() {
        this.isAccountExpired = true;
    }

    public void unexpireAccount() {
        this.isAccountExpired = false;
    }

    public User(String username, String password, String singleRole) {
        this.username = username;
        this.password = password;
        // Default to "USER" role if singleRole is null or empty, else use the provided singleRole
        this.roles = (singleRole != null && !singleRole.isEmpty()) ? 
                     new Role[]{Role.valueOf(singleRole)} : 
                     new Role[]{Role.USER};  // Default to "USER" role if not provided
    }
    
    
    
}
