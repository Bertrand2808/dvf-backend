package com.bezkoder.spring.jpa.h2.service;


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
    public void genererRapportTransactions() throws FileNotFoundException {
        // Spécifiez le chemin où le PDF sera sauvegardé
        String path = "E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/rapport.pdf";

        // Crée un écrivain de PDF pour écrire dans le fichier spécifié
        PdfWriter writer = new PdfWriter(path);

        // Crée un document PDF en utilisant l'écrivain PDF
        PdfDocument pdf = new PdfDocument(writer);

        // Crée un document iTextPDF pour organiser le contenu
        Document document = new Document(pdf);

        // Récupère la liste des transactions depuis le référentiel (repository)
        List<Transaction> transactions = transactionRepository.findAll();

        // Parcours toutes les transactions et ajoute leurs détails au document PDF
        for (Transaction transaction : transactions) {
            document.add(new Paragraph("ID Mutation: " + transaction.getIdMutation()));
            // Vous pouvez ajouter d'autres détails de la transaction ici si nécessaire
            document.add(new Paragraph("--------------------------------------"));
        }
        document.close();
    }
}