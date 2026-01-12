package com.example.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

@Entity
@Table(name = "subject")
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String className; // e.g., "10th"

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "subject_ids_list")
    private List<Long> subjectIds; 
}