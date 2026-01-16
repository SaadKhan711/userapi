package com.example.userapi.repository;

import com.example.userapi.entity.MarkList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkListRepository extends JpaRepository<MarkList, Long> {
}