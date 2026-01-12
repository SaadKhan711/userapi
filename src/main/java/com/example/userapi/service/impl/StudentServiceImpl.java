package com.example.userapi.service.impl;

import com.example.userapi.dto.StudentRegistrationDto;
import com.example.userapi.entity.StudentRecord;
import com.example.userapi.repository.StudentRecordRepository;
import com.example.userapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRecordRepository studentRepo;

    @Override
    public StudentRecord registerStudent(StudentRegistrationDto dto) {
        StudentRecord student = new StudentRecord();

        student.setFname(dto.getFname());
        student.setLname(dto.getLname());
        student.setEmail(dto.getEmail());
        student.setDob(dto.getDob());
        student.setFatherName(dto.getFatherName());
        student.setMotherName(dto.getMotherName());
        student.setAssignedClass(dto.getAssignedClass());

        student.setStatus("ACTIVE");

        String year = String.valueOf(Year.now().getValue());
        String uniqueCode = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String generatedRegId = "REG-" + year + "-" + uniqueCode;
        
        student.setRegistrationId(generatedRegId);

        return studentRepo.save(student);
    }
}