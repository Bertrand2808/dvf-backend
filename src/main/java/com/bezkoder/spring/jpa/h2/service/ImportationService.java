package com.bezkoder.spring.jpa.h2.service;
import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImportationService {
    private final TransactionRepository transactionsRepository;

    @Autowired
    public ImportationService(TransactionRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    // Position actuelle dans le fichier CSV
    private int currentLine = 0;
//    @Scheduled(fixedDelay = 300000)
//    public void importCsvData() {
//        System.out.println("Importing CSV data at " + java.time.LocalTime.now());
//        try {
//            Reader reader = Files.newBufferedReader(Paths.get("E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/full.csv"));
//            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
//                    .withType(Transaction.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//
//            List<Transaction> transactions = csvToBean.parse();
//            int endLine = Math.min(currentLine + 100000, transactions.size());
//            for (Transaction transaction : transactions.subList(currentLine, endLine)) {
//                transactionsRepository.save(transaction);
//            }
//            System.out.println("Importation de " + (endLine - currentLine) + " lignes");
//            currentLine = endLine;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Scheduled(fixedDelay = 300000)
    public void importCsvData() {
        System.out.println("Importation des données CSV à " + java.time.LocalTime.now());
        Path path = Paths.get("E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/full.csv");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            int lineCount = 0;
            for (Transaction transaction : csvToBean) {
                if (lineCount >= currentLine && lineCount < currentLine + 100000) {
                    transactionsRepository.save(transaction);
                }
                lineCount++;
                if (lineCount == currentLine + 100000) break;
            }
            currentLine += 100000;
            System.out.println("Importation de " + Math.min(100000, lineCount - currentLine) + " lignes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
