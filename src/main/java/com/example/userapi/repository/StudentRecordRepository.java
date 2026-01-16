package com.example.userapi.repository;
import com.example.userapi.entity.StudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {

	StudentRecord findByRegistrationId(String registrationId);}