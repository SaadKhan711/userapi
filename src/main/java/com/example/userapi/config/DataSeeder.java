package com.example.userapi.config;

import com.example.userapi.entity.Subject;
import com.example.userapi.entity.SubjectName;
import com.example.userapi.repository.SubjectNameRepository;
import com.example.userapi.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private SubjectNameRepository subjectNameRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Override
    @Transactional 
    public void run(String... args) throws Exception {
        System.out.println("--- Seeding Master Data ---");

        List<String> allSubjects = Arrays.asList(
            "Mathematics", "English", "Hindi", "Science", "Social Science",
            
            "Physics", "Chemistry", "Biology", "Computer Science", 
            "History", "Geography", "Economics", "Environmental Studies (EVS)"
        );

        for (String s : allSubjects) {
            if (subjectNameRepo.findBySubjectName(s) == null) {
                SubjectName sn = new SubjectName();
                sn.setSubjectName(s);
                subjectNameRepo.save(sn);
            }
        }
 
        List<String> middleSchoolSubjects = Arrays.asList(
            "Mathematics", "English", "Hindi", "Science", "Social Science", "Environmental Studies (EVS)"
        );
        createClass("5th", middleSchoolSubjects);
        createClass("6th", middleSchoolSubjects);
        createClass("7th", middleSchoolSubjects);
        createClass("8th", middleSchoolSubjects);

        List<String> highSchoolSubjects = Arrays.asList(
            "Mathematics", "English", "Hindi", "Science", "Social Science", "Computer Science"
        );
        createClass("9th", highSchoolSubjects);
        createClass("10th", highSchoolSubjects);

        List<String> scienceStream = Arrays.asList(
            "Mathematics", "Physics", "Chemistry", "English", "Computer Science"
        );
        createClass("11th", scienceStream);
        createClass("12th", scienceStream);
        
        System.out.println("--- Seeding Complete: Classes 5-12 Created ---");
    }

    private void createClass(String className, List<String> subjectsWanted) {
        if (!subjectRepo.existsByClassName(className)) {
            Subject cls = new Subject();
            cls.setClassName(className);

            List<Long> ids = new ArrayList<>();
            for (String name : subjectsWanted) {
                SubjectName sn = subjectNameRepo.findBySubjectName(name);
                if (sn != null) {
                    ids.add(sn.getId());
                }
            }
            
            cls.setSubjectIds(ids); 
            subjectRepo.save(cls);
            System.out.println("Created " + className + " with " + ids.size() + " subjects.");
        }
    }
}