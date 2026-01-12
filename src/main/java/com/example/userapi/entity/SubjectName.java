package com.example.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subject_name")
@Data
public class SubjectName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String subjectName; 
}