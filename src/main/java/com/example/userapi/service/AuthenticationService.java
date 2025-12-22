package com.example.userapi.service;

import com.example.userapi.config.JwtService;
import com.example.userapi.dto.request.LoginRequest;
import com.example.userapi.dto.request.SignupRequest;
import com.example.userapi.dto.response.JwtResponse;
import com.example.userapi.entity.Role;
import com.example.userapi.entity.User;
import com.example.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(SignupRequest request) {
        var user = User.builder()
                .username(request.getUsername())  
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(1) 
                .build();
        
        repository.save(user);

        return "User registered successfully! Please login to get your token.";
    }

    public JwtResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), 
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        
        var jwtToken = jwtService.generateToken(user);
        
        return new JwtResponse(jwtToken, user.getUsername());
    }

    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}