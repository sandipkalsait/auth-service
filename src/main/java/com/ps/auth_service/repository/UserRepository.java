package com.ps.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps.auth_service.entities.User;



@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
    
}
