package com.example.userapi.repository;

import com.example.userapi.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    Subject findByClassName(String className);
    
    boolean existsByClassName(String className);
}