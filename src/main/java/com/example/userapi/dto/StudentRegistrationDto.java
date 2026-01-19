package com.example.userapi.dto;

import lombok.Data;

@Data
public class StudentRegistrationDto {
    private String fname;
    private String lname;
    private String email;
    private String dob;
    private String fatherName;
    private String motherName;
    private String assignedClass;
    private String address;
    private String gender;
    private String contactNumber;
}