package com.example.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "year_mark")
@Data
public class YearMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationId;
    private String passingYear;
    private Double percentage;

    private String grade; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mark_list_id", referencedColumnName = "id")
    private MarkList markList;
}