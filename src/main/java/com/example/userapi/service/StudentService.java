package com.example.userapi.service;

import com.example.userapi.dto.StudentRegistrationDto;
import com.example.userapi.entity.StudentRecord;

public interface StudentService {
    StudentRecord registerStudent(StudentRegistrationDto dto);
}