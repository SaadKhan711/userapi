package com.example.userapi.service.impl;

import com.example.userapi.dto.request.SignupRequest;
import com.example.userapi.dto.response.UserResponse;
import com.example.userapi.entity.Role;
import com.example.userapi.entity.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.AuthenticationService;
import com.example.userapi.service.UserService;
import com.example.userapi.util.FileExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Value("${app.export.path}")
    private String exportPath;

    @Override
    public String exportUsersLocally(String format, Long userId) {
        User currentUser = authenticationService.getAuthenticatedUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access Denied: Only Admins can export files to the server.");
        }

        List<User> userList;
        String filename;

        if (userId != null) {
            User targetUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            userList = Collections.singletonList(targetUser);
            filename = targetUser.getUsername() + "_report_" + System.currentTimeMillis();
        } else {
            userList = userRepository.findAll();
            filename = "all_users_report_" + System.currentTimeMillis();
        }

        String fullPath = "";
        switch (format.toLowerCase()) {
            case "excel":
                fullPath = exportPath + "/" + filename + ".xlsx";
                FileExporter.saveToExcel(userList, fullPath);
                break;

            case "csv":
                fullPath = exportPath + "/" + filename + ".csv";
                FileExporter.saveToCsv(userList, fullPath);
                break;

            case "pdf":
                fullPath = exportPath + "/" + filename + ".pdf";
                FileExporter.saveToPdf(userList, fullPath);
                break;

            default:
                throw new RuntimeException("Unknown format: " + format);
        }

        return "Success! File saved at: " + fullPath;
    }

    @Override
    public UserResponse createUser(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) 
                .status(1)      
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User currentUser = authenticationService.getAuthenticatedUser();

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Access Denied: You can only view your own profile.");
        }

        if (targetUser.getStatus() != null && targetUser.getStatus() == 0) {
            throw new RuntimeException("User not found (Deleted)");
        }

        return mapToResponse(targetUser);
    }

    @Override
    public UserResponse softDeleteUser(Long id) {
        User currentUser = authenticationService.getAuthenticatedUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access Denied: Only Admins can delete users.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setStatus(0); // Set to Deleted
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse restoreUser(Long id) {
        User currentUser = authenticationService.getAuthenticatedUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access Denied: Only Admins can restore users.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id" + id));
        
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new RuntimeException("User is already active");
        }
        
        user.setStatus(1); // Set to Active
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, SignupRequest request) {
        User currentUser = authenticationService.getAuthenticatedUser();
        
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
             throw new RuntimeException("Access Denied: You can only update your own profile.");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (existingUser.getStatus() != null && existingUser.getStatus() == 0) {
            throw new RuntimeException("User is deleted and cannot be updated");
        }

        if (!request.getUsername().equals(existingUser.getUsername())) {
             if (userRepository.existsByUsername(request.getUsername())) {
                 throw new RuntimeException("Username already taken!");
             }
        }
        if (!request.getEmail().equals(existingUser.getEmail())) {
             if (userRepository.existsByEmail(request.getEmail())) {
                 throw new RuntimeException("Email already in use!");
             }
        }
        
        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(existingUser);
        return mapToResponse(updatedUser);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setStatus(user.getStatus());
        return response;
    }
}