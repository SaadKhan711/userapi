package com.example.userapi.controller;

import com.example.userapi.dto.ApiResponse;
import com.example.userapi.dto.ResultInputDto;
import com.example.userapi.dto.StudentRegistrationDto;
import com.example.userapi.entity.StudentRecord;
import com.example.userapi.entity.YearMark;
import com.example.userapi.service.ResultService;
import com.example.userapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ResultService resultService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StudentRecord>> addStudent(@RequestBody StudentRegistrationDto dto) {
    
        StudentRecord savedStudent = studentService.registerStudent(dto);
        
        return ResponseEntity.ok(ApiResponse.success("Student Registered Successfully", savedStudent));
    }

    @PostMapping("/results/add")
    public ResponseEntity<ApiResponse<YearMark>> addResult(@RequestBody ResultInputDto dto) {
        YearMark savedResult = resultService.processResult(dto);
        
        return ResponseEntity.ok(ApiResponse.success("Result Added Successfully", savedResult));
    }
}