package com.ps.auth_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UsernameNotFoundException globally across all controllers.
     * 
     * @param ex The exception to handle.
     * @return ResponseEntity with error message and status.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.trace("Entering handleUsernameNotFoundException method");
        log.error("Username not found exception: {}", ex.getMessage());
        log.trace("Exiting handleUsernameNotFoundException method");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Return 404 status
    }

    /**
     * Handles AccessDeniedException globally across all controllers.
     * 
     * @param ex The exception to handle.
     * @return ResponseEntity with error message and status.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.trace("Entering handleAccessDeniedException method");
        log.error("Access denied exception: {}", ex.getMessage());
        log.trace("Exiting handleAccessDeniedException method");
        return new ResponseEntity<>("Access Denied. You do not have permission.", HttpStatus.FORBIDDEN); // Return 403 status
    }

    /**
     * Handles NullPointerException globally across all controllers.
     * 
     * @param ex The exception to handle.
     * @return ResponseEntity with error message and status.
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        log.trace("Entering handleNullPointerException method");
        log.error("Null pointer exception: {}", ex.getMessage(), ex);
        log.trace("Exiting handleNullPointerException method");
        return new ResponseEntity<>("A null pointer error occurred. Please check the request.", HttpStatus.BAD_REQUEST); // Return 400 status
    }

    /**
     * Handles IllegalArgumentException globally across all controllers.
     * 
     * @param ex The exception to handle.
     * @return ResponseEntity with error message and status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.trace("Entering handleIllegalArgumentException method");
        log.error("Illegal argument exception: {}", ex.getMessage(), ex);
        log.trace("Exiting handleIllegalArgumentException method");
        return new ResponseEntity<>("Invalid arguments provided. Please check the input.", HttpStatus.BAD_REQUEST); // Return 400 status
    }

    /**
     * Handles any general exceptions globally across all controllers.
     * 
     * @param ex The exception to handle.
     * @return ResponseEntity with error message and status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.trace("Entering handleGenericException method");
        log.error("An error occurred: {}", ex.getMessage(), ex);
        log.trace("Exiting handleGenericException method");
        return new ResponseEntity<>("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 status
    }
}
