package com.bezkoder.spring.jpa.h2.service;


import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.controller.RechercheController;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PdfGenerateurService {
    private final Logger logger = Logger.getLogger(PdfGenerateurService.class.getName());
    private static final double EARTH_RADIUS = 6371e3; // en mètres
    private final TransactionRepository transactionRepository;

    public byte[] pdfGenerate(String path, double latitude, double longitude, double rayon) throws IOException {
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("Rapport des Transactions comprises dans le rayon de " + rayon + " mètres autour de la position :"));
        document.add(new Paragraph("Latitude saisie : " + latitude));
        document.add(new Paragraph("Longitude saisie : " + longitude));
        document.add(new Paragraph("--------------------------------------"));
        List<Transaction> transactions = transactionRepository.findAll();
        if(logger.isLoggable(Level.INFO)) {
            logger.info(MessageFormat.format("Nombre de transactions trouvées : {0}", transactions.size()));
        }
        List<Transaction> transactionsDansRayon = transactions.stream()
                .filter(t -> t.getLatitude() != null && t.getLongitude() != null)
                .filter(t -> calculerDistance(latitude, longitude, t.getLatitude(), t.getLongitude()) <= rayon)
                .toList();
        if(logger.isLoggable(Level.INFO)) {
            logger.info(MessageFormat.format("Nombre de transactions trouvées dans le rayon de {0} mètres : {1}", rayon, transactionsDansRayon.size()));
        }
        if (transactionsDansRayon.isEmpty()) {
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
        Path pdfPath = Paths.get(path);
        byte[] pdfBytes = Files.readAllBytes(pdfPath);
        Files.delete(pdfPath);
        logger.info("PDF généré et supprimé du disque.");
        return pdfBytes;
    }
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance en mètres
    }
}