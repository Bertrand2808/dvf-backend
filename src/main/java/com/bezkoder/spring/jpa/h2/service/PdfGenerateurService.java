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

    private final TransactionRepository transactionRepository;
    @Autowired
    public PdfGenerateurService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
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
}