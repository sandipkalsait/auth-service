package com.ps.auth_service.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ps.auth_service.service.JwtService;
import com.ps.auth_service.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Request received: {} {}", request.getMethod(), request.getRequestURI());

        try {
            // Extract JWT token from Authorization header
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7).trim();
                username = jwtService.extractUsername(token);
                log.info("JWT token extracted for username: {}", username);
            } else {
                log.warn("Authorization header is missing or invalid.");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("No existing authentication found, attempting authentication for username: {}", username);
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    log.info("JWT token validated successfully for username: {}", username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Authentication set in SecurityContext for username: {}", username);
                } else {
                    log.warn("Invalid JWT token for username: {}", username);
                }
            } else {
                log.info("No valid username or authentication found.");
            }

            // Proceed to the next filter or request handler
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error during JWT filtering process: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Internal Server Error
            response.getWriter().write("Internal server error during authentication");
        }

        log.info("Response status: {}", response.getStatus());
    }
}
