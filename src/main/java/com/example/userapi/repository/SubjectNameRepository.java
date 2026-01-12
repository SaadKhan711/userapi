package com.example.userapi.repository;
import com.example.userapi.entity.SubjectName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectNameRepository extends JpaRepository<SubjectName, Long> {
    SubjectName findBySubjectName(String subjectName);
}