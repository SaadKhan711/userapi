package com.example.userapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalMark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_mark_id")
    private YearMark yearMark;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
}