package com.example.userapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResultInputDto {
    private String registrationId;
    private String passingYear;
    private List<SubjectMarkDto> subjects;

    @Data
    public static class SubjectMarkDto {
        private Long subjectId;
        private Integer marks;
    }
}