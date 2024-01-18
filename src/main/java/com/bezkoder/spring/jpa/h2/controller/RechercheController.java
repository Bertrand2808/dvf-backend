package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.service.PdfGenerateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class RechercheController {
    @Autowired
    private PdfGenerateurService pdfGeneratorService;

    @GetMapping("/transactions")
    public ResponseEntity<byte[]> rechercher(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "rayon") double rayon) throws IOException {

        // Generate PDF with transactions in the specified radius
        byte[] pdfBytes = pdfGeneratorService.genererRapportTransactions(latitude, longitude, rayon);

        // Set the appropriate headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport.pdf");

        // Return the byte array as the response body
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
