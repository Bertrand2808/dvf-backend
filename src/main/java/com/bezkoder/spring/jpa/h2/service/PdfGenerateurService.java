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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PdfGenerateurService {
    private static final double EARTH_RADIUS = 6371e3; // en mètres
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

    public byte[] pdfGenerate(String path, double latitude, double longitude, double rayon) throws IOException {
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("Rapport des Transactions comprises dans le rayon de " + rayon + " mètres autour de la position :"));
        document.add(new Paragraph("Latitude saisie : " + latitude));
        document.add(new Paragraph("Longitude saisie : " + longitude));
        document.add(new Paragraph("--------------------------------------"));
        List<Transaction> transactions = transactionRepository.findAll();
        System.out.println("Nombre de transactions récupérées : " + transactions.size());
        List<Transaction> transactionsDansRayon = transactions.stream()
                .filter(t -> calculerDistance(latitude, longitude, t.getLatitude(), t.getLongitude()) <= rayon)
                .collect(Collectors.toList());
        System.out.println("Nombre de transactions dans le rayon : " + transactionsDansRayon.size());
        if (transactionsDansRayon.size() == 0) {
            document.add(new Paragraph("Aucune transaction trouvée dans le rayon de " + rayon + " mètres autour de la position saisie."));
        } else {
            for (Transaction transaction : transactionsDansRayon) {
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
                if (transaction.getCodePostal() != null) {
                    document.add(new Paragraph("Code Postal : " + transaction.getCodePostal()));
                }
                if (transaction.getNomCommune() != null) {
                    document.add(new Paragraph("Nom de la commune : " + transaction.getNomCommune()));
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
        }
        document.close();
        byte[] pdfBytes = Files.readAllBytes(Paths.get(path));
        Files.delete(Paths.get(path));
        System.out.println("PDF généré : " + pdfBytes);
        return pdfBytes;
    }

//    public String pdfGenerate(double latitude, double longitude, double rayon) throws IOException {
//        String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";
//        String path = "src/main/resources/" + fileName;
//
//        PdfWriter writer = new PdfWriter(path);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf);
//
//        // Exemple de contenu - ajouter le contenu réel du PDF ici
//        document.add(new Paragraph("Rapport des Transactions"));
//        document.add(new Paragraph("Latitude: " + latitude));
//        document.add(new Paragraph("Longitude: " + longitude));
//        document.add(new Paragraph("Rayon: " + rayon));
//
//        document.close();
//        return path;
//    }
    public void sendPdfToQueue() {
        System.out.println("Envoi du pdf dans la queue");
        jmsMessageSender.send("pdfQueue", "pdf");
    }
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        System.out.println("Distance : " + EARTH_RADIUS * c);
        return EARTH_RADIUS * c; // Distance en mètres
    }
}