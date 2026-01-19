package com.example.userapi.controller;

import com.example.userapi.dto.request.LoginRequest;
import com.example.userapi.dto.request.SignupRequest;
import com.example.userapi.dto.response.JwtResponse;
import com.example.userapi.service.AuthenticationService;
import com.example.userapi.validation.AppValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final AppValidator Validator;

    @InitBinder("signupRequest")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(Validator);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}