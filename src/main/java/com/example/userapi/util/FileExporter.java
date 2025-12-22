package com.example.userapi.util;

import com.example.userapi.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileExporter {

    public static void saveToExcel(List<User> users, String fullPath) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream out = new FileOutputStream(fullPath)) {

            Sheet sheet = workbook.createSheet("Users");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Username", "Email", "Status", "Role"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getStatus() != null ? user.getStatus().toString() : "N/A");
                row.createCell(4).setCellValue(user.getRole().name());
            }

            workbook.write(out);
            System.out.println("Excel file saved successfully: " + fullPath);

        } catch (IOException e) {
            throw new RuntimeException("Excel export failed: " + e.getMessage());
        }
    }

    public static void saveToCsv(List<User> users, String fullPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            
          
            writer.println("ID,Username,Email,Status,Role");

            
            for (User user : users) {
                writer.printf("%s,%s,%s,%s,%s%n",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatus(),
                        user.getRole());
            }
            System.out.println("CSV file saved successfully: " + fullPath);

        } catch (IOException e) {
            throw new RuntimeException("CSV export failed: " + e.getMessage());
        }
    }

    public static void saveToPdf(List<User> users, String fullPath) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            Paragraph title = new Paragraph("User Report", font);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5); 
            table.setWidthPercentage(100f);

            String[] headers = {"ID", "Username", "Email", "Status", "Role"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                table.addCell(cell);
            }

            for (User user : users) {
                table.addCell(String.valueOf(user.getId()));
                table.addCell(user.getUsername());
                table.addCell(user.getEmail());
                table.addCell(String.valueOf(user.getStatus()));
                table.addCell(user.getRole().name());
            }

            document.add(table);
            document.close();
            System.out.println("PDF file saved successfully: " + fullPath);

        } catch (Exception e) {
            throw new RuntimeException("PDF export failed: " + e.getMessage());
        }
    }
}