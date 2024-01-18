package com.bezkoder.spring.jpa.h2.service;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class PdfGenerateurService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Méthode pour générer un rapport PDF des transactions

    public byte[] genererRapportTransactions(double latitude, double longitude, double rayon) throws IOException {
        // Specify the path where the PDF will be saved
        String path = "src/main/resources/rapport.pdf";

        // Create a PDF writer to write to the specified file
        PdfWriter writer = new PdfWriter(path);

        // Create a PDF document using the PDF writer
        PdfDocument pdf = new PdfDocument(writer);

        // Create an iTextPDF document to organize the content
        Document document = new Document(pdf);

        document.add(new Paragraph("Rapport des Transactions dans un Rayon Spécifié"));
        document.add(new Paragraph("Latitude voulu : " + latitude));
        document.add(new Paragraph("Longitude voulu : " + longitude));
        document.add(new Paragraph("Rayon voulu : " + rayon));
        document.add(new Paragraph("----------------------------------------------------------------------------------------------------------"));

        // Retrieve the list of transactions within the specified radius
        List<Transaction> transactions = transactionRepository.findAll();

        System.out.println("Number of transactions fetched: " + transactions.size());

        // Iterate over transactions and add essential details to the PDF document
        for (Transaction transaction : transactions) {
            document.add(new Paragraph("ID Mutation: " + transaction.getIdMutation()));
            if (transaction.getNombrePiecesPrincipales() != null) {
                document.add(new Paragraph("Nombre de Pièces Principales: " + transaction.getNombrePiecesPrincipales()));
            }
            if (transaction.getSurfaceTerrain() != null) {
                document.add(new Paragraph("Surface du Terrain: " + transaction.getSurfaceTerrain()));
            }
            if (transaction.getLongitude() != null && transaction.getLatitude() != null) {
                document.add(new Paragraph("Long: " + transaction.getLongitude() + " / Lat: " + transaction.getLatitude()));
            }
            if (transaction.getValeurFonciere() != null) {
                document.add(new Paragraph("Valeur Foncière: " + transaction.getValeurFonciere() + " Euros"));
            }
            // Add a separator line between transactions
            document.add(new Paragraph("--------------------------------------"));
        }


        // Close the document
        document.close();

        byte[] pdfBytes = Files.readAllBytes(Paths.get(path));

        // Delete the file after reading its content
        Files.delete(Paths.get(path));

        return pdfBytes;
    }

}