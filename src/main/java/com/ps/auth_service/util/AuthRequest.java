package com.ps.auth_service.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
    private Optional<String> role;

}
