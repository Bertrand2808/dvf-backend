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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImportationService {
    private static final int BATCH_SIZE = 500000; // nombre de lignes à importer à la fois
    private static final int DELAY = 60000; // 5 minutes
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
        Path path = Paths.get(csvFilePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Transaction> transactions = csvToBean.parse();

            if (currentLineInCsv >= transactions.size()) {
                logger.info("Fin du fichier CSV atteinte. Arrêt de l'importation.");
                return; // Sortie anticipée si on a atteint ou dépassé la fin du fichier
            }

            // Détermine le sous-ensemble des transactions à importer
            List<Transaction> batch = transactions.subList(currentLineInCsv, Math.min(currentLineInCsv + BATCH_SIZE, transactions.size()));
            transactionsRepository.saveAll(batch); // Utilise saveAll pour améliorer les performances
            currentLineInCsv += BATCH_SIZE;

            if (logger.isLoggable(Level.INFO)) {
                logger.info(MessageFormat.format("Importation des données CSV terminée à la ligne {0}", currentLineInCsv));
            }
        } catch (IOException e) {
            throw new CsvImportException("Erreur lors de l'importation des données CSV.", e);
        }
    }
}
