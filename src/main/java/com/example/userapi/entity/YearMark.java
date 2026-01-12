package com.example.userapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YearMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passingYear;
    private Double percentage;
    
    private String registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_reg_id", referencedColumnName = "registrationId")
    private StudentRecord student;

    @OneToMany(mappedBy = "yearMark", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MarkList> markLists;
}
