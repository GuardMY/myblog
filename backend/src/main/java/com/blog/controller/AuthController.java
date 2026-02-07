package com.blog.controller;

import com.blog.entity.User;
import com.blog.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            System.out.println("Login attempt for username: " + username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            System.out.println("Authentication successful for: " + authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(Map.of("message", "Login successful", "user", username), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Login failed for username: " + username + ", error: " + e.getMessage());
            return new ResponseEntity<>(Map.of("error", "Invalid credentials"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(Map.of("message", "Logout successful"), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // 尝试从SecurityContext获取认证信息
        Authentication auth = authentication;
        if (auth == null) {
            auth = SecurityContextHolder.getContext().getAuthentication();
        }

        if (auth == null || auth.getPrincipal() == null || auth.getName().equals("anonymousUser")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = auth.getName();
        System.out.println("Getting user for username: " + username);
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}