package com.example.userapi.service;

import com.example.userapi.entity.UserDetails;
import com.example.userapi.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final UserDetailsRepository detailsRepository;

    @Value("${app.export.path}")
    private String exportPath;

    public String processCsv(MultipartFile file) {
        List<UserDetails> detailsList = new ArrayList<>();
        
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            csvReader.readNext(); 
            
            while ((line = csvReader.readNext()) != null) {
                UserDetails details = UserDetails.builder()
                        .phoneNumber(line[0])
                        .address(line[1]) 
                        .city(line[2])
                        .department(line[3])
                        .filePath("Imported from CSV")
                        .build();
                detailsList.add(details);
            }
            
            detailsRepository.saveAll(detailsList);
            return "CSV Processed! Saved " + detailsList.size() + " records to DB.";

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("CSV Error: " + e.getMessage());
        }
    }

    public String processExcel(MultipartFile file) {
        List<UserDetails> detailsList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            DataFormatter dataFormatter = new DataFormatter();

            for (Row row : sheet) {
         
                if (row.getRowNum() < 2) continue; 

                UserDetails details = new UserDetails();
                

                
                if (row.getCell(1) != null) {
                    String phone = dataFormatter.formatCellValue(row.getCell(1));
                    details.setPhoneNumber(phone);
                }
                
                if (row.getCell(2) != null) {
                    details.setAddress(row.getCell(2).toString());
                }
                
                if (row.getCell(3) != null) {
                    details.setCity(row.getCell(3).toString());
                }
                
                if (row.getCell(4) != null) {
                    details.setDepartment(row.getCell(4).toString());
                }

                details.setFilePath("Imported from Excel");
                detailsList.add(details);
            }

            detailsRepository.saveAll(detailsList);
            return "Excel Processed! Saved " + detailsList.size() + " records to DB.";

        } catch (IOException e) {
            throw new RuntimeException("Excel Error: " + e.getMessage());
        }
    } 

    public String saveDataToXml() {
        try {
            List<UserDetails> allData = detailsRepository.findAll();

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT); 

            File directory = new File(exportPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filename = "database_dump_" + System.currentTimeMillis() + ".xml";
            File xmlFile = new File(exportPath + "/" + filename);

            xmlMapper.writeValue(xmlFile, allData);

            return "Success! Database saved as XML at: " + xmlFile.getAbsolutePath();

        } catch (IOException e) {
            throw new RuntimeException("XML Export Error: " + e.getMessage());
        }
    }
}