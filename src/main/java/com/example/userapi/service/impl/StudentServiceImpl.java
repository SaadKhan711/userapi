package com.example.userapi.service.impl;

import com.example.userapi.dto.StudentRegistrationDto;
import com.example.userapi.entity.StudentRecord;
import com.example.userapi.exception.ValidationException;
import com.example.userapi.repository.StudentRecordRepository;
import com.example.userapi.service.StudentService;
import com.example.userapi.validation.AppValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.time.LocalDate;
import java.util.Random;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRecordRepository studentRepo;

    @Autowired
    private AppValidator validator; 

    @Override
    public StudentRecord registerStudent(StudentRegistrationDto dto) {
        
       
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "studentRegistrationDto");
        validator.validate(dto, bindingResult);

        
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        StudentRecord student = new StudentRecord();
        student.setFname(dto.getFname());
        student.setLname(dto.getLname());
        student.setEmail(dto.getEmail());
        student.setDob(dto.getDob());
        student.setFatherName(dto.getFatherName());
        student.setMotherName(dto.getMotherName());
        student.setAssignedClass(dto.getAssignedClass());
        student.setAddress(dto.getAddress());
        student.setGender(dto.getGender());
        student.setContactNumber(dto.getContactNumber());

        String regId = generateRegistrationId(dto.getFname(), dto.getLname());
        student.setRegistrationId(regId);

        return studentRepo.save(student);
    }

    private String generateRegistrationId(String fname, String lname) {
        int year = LocalDate.now().getYear() + 1; 
        String f = fname.length() >= 1 ? fname.substring(0, 1).toUpperCase() : "X";
        String l = lname.length() >= 1 ? lname.substring(0, 1).toUpperCase() : "X";
        String randomCode = String.format("%04d", new Random().nextInt(10000));
        return "REG-" + year + "-" + f + l + randomCode;
    }
}