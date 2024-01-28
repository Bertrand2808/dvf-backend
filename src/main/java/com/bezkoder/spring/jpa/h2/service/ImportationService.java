package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.exception.CsvImportException;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImportationService {
    private static final int BATCH_SIZE = 100000; // nombre de lignes à importer à la fois
    private static final int DELAY = 300000; // 5 minutes
    private final Logger logger = Logger.getLogger(ImportationService.class.getName());
    private final TransactionRepository transactionsRepository;
    @Value("${csvFilePath}")
    private String csvFilePath;
    @Autowired
    public ImportationService(TransactionRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }
    private int currentLineInCsv = 0;
    /**
     * Importe les données CSV dans la base de données
     * 100000 lignes à la fois chaque 5 minutes
     */
    @Scheduled(fixedDelay = DELAY)
    public void importCsvData() {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(MessageFormat.format("Importation des données CSV à partir de la ligne {0}", currentLineInCsv));
        }
        Path path = Paths.get(csvFilePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            int lineCount = 0;
            for (Transaction transaction : csvToBean) {
                if (lineCount >= currentLineInCsv && lineCount < currentLineInCsv + BATCH_SIZE) {
                    transactionsRepository.save(transaction);
                }
                lineCount++;
                if (lineCount == currentLineInCsv + BATCH_SIZE) break;
            }
            currentLineInCsv += BATCH_SIZE;
            if(logger.isLoggable(Level.INFO)) {
                logger.info(MessageFormat.format("Importation des données CSV terminée à la ligne {0}", currentLineInCsv));
            }
        } catch (IOException e) {
            throw new CsvImportException("Erreur lors de l'importation des données CSV.", e);
        }
    }
}
