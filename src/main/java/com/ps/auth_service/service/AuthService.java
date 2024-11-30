package com.ps.auth_service.service;

import com.ps.auth_service.entities.User;

public interface AuthService {
    User registerUser(User user);

    boolean verifyUser(User user);
}
