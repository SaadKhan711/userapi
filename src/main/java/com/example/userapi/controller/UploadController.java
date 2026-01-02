package com.example.userapi.controller;

import com.example.userapi.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class UploadController {

    private final FileProcessingService processingService;
    
    @PostMapping(value = "/upload/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(processingService.processCsv(file));
    }

 
    @PostMapping(value = "/upload/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(processingService.processExcel(file));
    }

    @GetMapping("/save/xml")
    public ResponseEntity<String> saveToXml() {
        return ResponseEntity.ok(processingService.saveDataToXml());
    }
    
}