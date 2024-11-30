package com.ps.auth_service.entities;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming the "role" field in the User entity is stored as a simple string like "USER" or "ADMIN"
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Return true for now, but this can be linked to a field in your User entity if needed
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Return true for now, but this can be linked to a field in your User entity if needed
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Return true for now, but this can be linked to a field in your User entity if needed
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Return true for now, but this can be linked to a field in your User entity if needed
        return true;
    }

    // Additional method to expose the User entity if needed
    public User getUser() {
        return user;
    }
}
