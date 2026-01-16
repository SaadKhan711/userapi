package com.example.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mark_list")
@Data
public class MarkList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationId; 

    @Column(name = "class_subject_ref_id")
    private Long subjectTableId; 

    private Double totalMarks; 

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "obtained_marks_list")
    private List<Integer> obtainedMarks;
    
    @OneToOne(mappedBy = "markList", cascade = CascadeType.ALL)
    @JsonIgnore
    private YearMark yearMark;
}