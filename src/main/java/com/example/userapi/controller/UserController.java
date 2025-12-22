package com.example.userapi.controller;

import com.example.userapi.dto.request.SignupRequest;
import com.example.userapi.dto.response.UserResponse;
import com.example.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/export/local")
    public ResponseEntity<String> exportLocal(
            @RequestParam String type,
            @RequestParam(required = false) Long userId 
    ) 
    {
    	
        String result = userService.exportUsersLocally(type, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody SignupRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        UserResponse response = userService.softDeleteUser(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable Long id) {
        UserResponse response = userService.restoreUser(id);
        return ResponseEntity.ok(response);
    }
}