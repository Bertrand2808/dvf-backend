package com.bezkoder.spring.jpa.h2.service;


import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
public class PdfGenerateurService {

    private final TransactionRepository transactionRepository;
    private final JmsMessageSender jmsMessageSender;

    public void genererRapportTransactions() throws FileNotFoundException {
        String path = "E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/rapport.pdf";
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            document.add(new Paragraph("ID Mutation: " + transaction.getIdMutation()));
            document.add(new Paragraph("--------------------------------------"));
        }
        document.close();
    }

    public byte[] pdfGenerate(double latitude, double longitude, double rayon) throws IOException {
        String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";
        String path = "src/main/resources/" + fileName;
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("Rapport des Transactions comprises dans le rayon de " + rayon + "mètres autour de la position :"));
        document.add(new Paragraph("Latitude saisie : " + latitude));
        document.add(new Paragraph("Longitude saisie : " + longitude));
        document.add(new Paragraph("--------------------------------------"));
        List<Transaction> transactions = transactionRepository.findAll();
        System.out.println("Nombre de transactions récupérées : " + transactions.size());
        for (Transaction transaction : transactions) {
            document.add(new Paragraph("ID Mutation: " + transaction.getIdMutation()));
            if (transaction.getDateMutation() != null) {
                document.add(new Paragraph("Date de mutation : " + transaction.getDateMutation()));
            }
            if (transaction.getNatureMutation() != null) {
                document.add(new Paragraph("Nature de la mutation : " + transaction.getNatureMutation()));
            }
            if (transaction.getAdresseNomVoie() != null) {
                document.add(new Paragraph("Adresse : " + transaction.getAdresseNomVoie()));
            }
            if (transaction.getNombrePiecesPrincipales() != null && transaction.getNombrePiecesPrincipales() != 0) {
                document.add(new Paragraph("Nombre de pièces principales : " + transaction.getNombrePiecesPrincipales()));
            }
            if (transaction.getSurfaceTerrain() != null) {
                document.add(new Paragraph("Surface du terrain : " + transaction.getSurfaceTerrain()));
            }
            if (transaction.getLatitude() != null && transaction.getLongitude() != null) {
                document.add(new Paragraph("Latitude : " + transaction.getLatitude() + " / Longitude : " + transaction.getLongitude()));
            }
            if (transaction.getValeurFonciere() != null) {
                document.add(new Paragraph("Valeur Foncière : " + transaction.getValeurFonciere()));
            }
            document.add(new Paragraph("--------------------------------------"));
        }
        document.close();
        byte[] pdfBytes = Files.readAllBytes(Paths.get(path));
        Files.delete(Paths.get(path));
        return pdfBytes;
    }

    public void sendPdfToQueue() {
        System.out.println("Envoi du pdf dans la queue");
        jmsMessageSender.send("pdfQueue", "pdf");
    }
}