package com.example.userapi.service.impl;

import com.example.userapi.dto.ResultInputDto;
import com.example.userapi.entity.MarkList;
import com.example.userapi.entity.StudentRecord;
import com.example.userapi.entity.Subject;
import com.example.userapi.entity.YearMark;
import com.example.userapi.exception.ValidationException;
import com.example.userapi.repository.StudentRecordRepository;
import com.example.userapi.repository.SubjectRepository;
import com.example.userapi.repository.YearMarkRepository;
import com.example.userapi.service.ResultService;
import com.example.userapi.validation.AppValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private YearMarkRepository yearMarkRepo;

    @Autowired
    private StudentRecordRepository studentRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private AppValidator validator; 

    @Override
    public YearMark processResult(ResultInputDto dto) {

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "resultInputDto");
        validator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        
        StudentRecord student = studentRepo.findByRegistrationId(dto.getRegistrationId());
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + dto.getRegistrationId());
        }

        Subject classInfo = subjectRepo.findByClassName(student.getAssignedClass());
        if (classInfo == null) {
            throw new RuntimeException("Class Curriculum not found for: " + student.getAssignedClass());
        }

        MarkList markList = new MarkList();
        markList.setRegistrationId(dto.getRegistrationId());
        markList.setSubjectTableId(classInfo.getId());

        List<Integer> marksOnly = new ArrayList<>();
        double totalObtained = 0;
        
        for (ResultInputDto.SubjectMarkDto item : dto.getSubjects()) {
            marksOnly.add(item.getMarks());
            totalObtained += item.getMarks();
        }
        
        markList.setObtainedMarks(marksOnly);
        markList.setTotalMarks(totalObtained);

        YearMark yearMark = new YearMark();
        yearMark.setRegistrationId(dto.getRegistrationId());
        yearMark.setPassingYear(dto.getPassingYear());

        double maxPossible = dto.getSubjects().size() * 100;
        double percentage = (totalObtained / maxPossible) * 100;
        percentage = Math.round(percentage * 100.0) / 100.0; 
        
        yearMark.setPercentage(percentage);
        yearMark.setGrade(calculateGrade(percentage));

        yearMark.setMarkList(markList);
        markList.setYearMark(yearMark);

        return yearMarkRepo.save(yearMark);
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A (Excellent)";
        if (percentage >= 75) return "B (Good)";
        if (percentage >= 60) return "C (Average)";
        if (percentage >= 40) return "D (Pass)";
        return "E (Fail)";
    }
}