package com.ps.auth_service.controller;

import org.springframework.web.bind.annotation.RestController;
import com.ps.auth_service.Bo.Device;
import com.ps.auth_service.entities.User;
import com.ps.auth_service.service.AuthService;
import com.ps.auth_service.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("v1")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    JwtService jwtService;

    private final List<Device> devices = new ArrayList<>();

    public AuthController() {
        // Initializing some mock devices
        devices.add(new Device("D001", "Smartphone A1", "Smartphone", "TechCorp", true));
        devices.add(new Device("D002", "Router X200", "Router", "NetGear", false));
        devices.add(new Device("D003", "Laptop Pro", "Laptop", "ComputeCo", true));
        devices.add(new Device("D004", "Tablet Z", "Tablet", "TabTech", false));
        devices.add(new Device("D005", "Smartwatch G3", "Wearable", "TimeTech", true));
    }

    // Endpoint to get the resource list and store it in the session
    @GetMapping("/getResource")
    public List<Device> getResource(HttpServletRequest request) {
        log.info("Received request to get resources: {}", request);
        HttpSession session = request.getSession(true);
        session.setAttribute("devices", devices);
        log.info("Session created with ID: {}", session.getId());
        return devices;
    }

    // Endpoint to retrieve devices from the session
    @GetMapping("/getSessionDevices")
    public Object getSessionDevices(HttpSession session) {
        Object sessionDevices = session.getAttribute("devices");
        if (sessionDevices != null) {
            log.info("Devices retrieved from session: {}", sessionDevices);
            return sessionDevices;
        } else {
            log.warn("No devices found in session.");
            return "No devices found in session.";
        }
    }

    // Endpoint to clear the session
    @GetMapping("/clearSession")
    public String clearSession(HttpSession session) {
        session.invalidate(); // Invalidate the session
        log.info("Session cleared successfully.");
        return "Session cleared successfully.";
    }

    // Endpoint to dynamically add a device and store it in the session
    @GetMapping("/addDevice")
    public List<Device> addDevice(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String manufacturer,
            @RequestParam boolean isOnline,
            HttpSession session) {
        Device newDevice = new Device(id, name, type, manufacturer, isOnline);
        devices.add(newDevice);
        session.setAttribute("devices", devices);
        log.info("New device added: {}", newDevice);
        return devices;
    }

    // Endpoint for registering a user
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        log.info("Received request to register user: {}", user);
        try {
            log.info("Registering user: {}", user);
            authService.registerUser(user);
            log.info("User successfully registered: {}", user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully: " + user);
        } catch (Exception e) {
            log.error("Error occurred during user registration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        log.info("Received login request for user: {}", user);
        try {
            boolean isAuthenticated = authService.verifyUser(user);
            
            if (isAuthenticated) {
                log.info("User authenticated successfully: {}", user.getUsername());
                String authToken = jwtService.generateToken(user.getUsername());
                log.info("Generated JWT token for user: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully: " + authToken);
            } else {
                log.warn("Invalid credentials for user: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
            }
        } catch (Exception e) {
            log.error("Error occurred during user login: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
