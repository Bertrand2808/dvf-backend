package com.bezkoder.spring.jpa.h2.service;


import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PdfGenerateurService {
    private final Logger logger = Logger.getLogger(PdfGenerateurService.class.getName());
    private static final double EARTH_RADIUS = 6371e3; // en mètres
    private final TransactionRepository transactionRepository;
    private final Queue<Runnable> tasks = new LinkedList<>();
    private final Object lock = new Object();

    public void enqueuePdfGeneration(String path, double latitude, double longitude, double rayon, Consumer<byte[]> onPdfGenerated) {
        Runnable task = () -> {
            try {
                byte[] pdfBytes = pdfGenerate(path, latitude, longitude, rayon);
                onPdfGenerated.accept(pdfBytes);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Erreur lors de la génération du PDF", e);
            }
        };
        synchronized (lock) {
            boolean wasEmpty = tasks.isEmpty();
            tasks.offer(task);
            if (wasEmpty) {
                logger.info("Nouvelle demande de génération de PDF reçue, traitement en cours...");
                processQueue(); // Appel pour traiter la tâche immédiatement si la file était vide
            } else {
                logger.info("Demande de génération de PDF mise en file d'attente.");
            }
        }
    }

    private void processQueue() {
        new Thread(() -> {
            Runnable task;
            while (true) {
                synchronized (lock) {
                    task = tasks.poll();
                    if (task == null) {
                        break;
                    }
                }
                task.run(); // Exécute la tâche en dehors du bloc synchronized pour permettre à d'autres tâches d'être enfilées
            }
        }).start();
    }

    private byte[] pdfGenerate(String path, double latitude, double longitude, double rayon) throws IOException {
//        PdfDocument pdf = createPdfDocument(path);
//        Document document = createDocument(pdf, latitude, longitude, rayon);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);

        // Création de l'objet PdfDocument
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document = createDocument(pdf, latitude, longitude, rayon);
        List<Transaction> transactionsDansRayon = getTransactionsInRadius(latitude, longitude, rayon);

        if (transactionsDansRayon.isEmpty()) {
            document.add(new Paragraph("Aucune transaction trouvée dans le rayon de " + rayon + " mètres autour de la position saisie."));
        } else {
            addTransactionsToDocument(document, transactionsDansRayon);
        }
        document.close();
//        byte[] pdfBytes = closeAndReadPdf(pdf, path);

        logger.info("PDF généré et supprimé du disque.");
        return byteArrayOutputStream.toByteArray();
    }

    private PdfDocument createPdfDocument(String path) throws IOException {
        PdfWriter writer = new PdfWriter(path);
        return new PdfDocument(writer);
    }

    private Document createDocument(PdfDocument pdf, double latitude, double longitude, double rayon) {
        Document document = new Document(pdf);
        document.add(new Paragraph("Rapport des Transactions comprises dans le rayon de " + rayon + " mètres autour de la position :"));
        document.add(new Paragraph("Latitude saisie : " + latitude));
        document.add(new Paragraph("Longitude saisie : " + longitude));
        document.add(new Paragraph("--------------------------------------"));
        return document;
    }

    private List<Transaction> getTransactionsInRadius(double latitude, double longitude, double rayon) {
        List<Transaction> transactions = transactionRepository.findAll();
        if(logger.isLoggable(Level.INFO)) {
            logger.info(MessageFormat.format("Nombre de transactions trouvées : {0}", transactions.size()));
        }
        return transactions.stream()
                .filter(t -> t.getLatitude() != null && t.getLongitude() != null)
                .filter(t -> calculerDistance(latitude, longitude, t.getLatitude(), t.getLongitude()) <= rayon)
                .toList();
    }

    private void addTransactionsToDocument(Document document, List<Transaction> transactionsDansRayon) {
        if (transactionsDansRayon.isEmpty()) {
            document.add(new Paragraph("Aucune transaction trouvée dans le rayon autour de la position saisie."));
        } else {
            for (Transaction transaction : transactionsDansRayon) {
                addTransactionDetails(document, transaction);
                document.add(new Paragraph("--------------------------------------"));
            }
        }
    }

    private void addTransactionDetails(Document document, Transaction transaction) {
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
    }

    private byte[] closeAndReadPdf(PdfDocument pdf, String path) throws IOException {
        PdfDocument document = pdf.getFirstPage().getDocument();
        document.close();

        Path pdfPath = Paths.get(path);
        byte[] pdfBytes = Files.readAllBytes(pdfPath);
        Files.delete(pdfPath);

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