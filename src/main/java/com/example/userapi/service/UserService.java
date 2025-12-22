package com.example.userapi.service;
import com.example.userapi.dto.request.SignupRequest;
import com.example.userapi.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(SignupRequest request);
    
    UserResponse getUserById(Long id);
    
    UserResponse softDeleteUser(Long id);
    
    UserResponse updateUser(Long id, SignupRequest request);
    
    UserResponse restoreUser(Long id);
            
    String exportUsersLocally(String format, Long userId);
}