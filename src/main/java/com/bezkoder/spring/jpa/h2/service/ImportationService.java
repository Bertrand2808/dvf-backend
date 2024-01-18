package com.bezkoder.spring.jpa.h2.service;
import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.Reader;
import java.nio.file.Files;
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
    @Scheduled(fixedDelay = 1000)
    public void importCsvData() {
        System.out.println("Importing CSV data...");
        System.out.println(java.time.LocalTime.now());
        try {
            Reader reader = Files.newBufferedReader(Paths.get("E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/full.csv"));
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Transaction> transactions = csvToBean.parse();
            int endLine = Math.min(currentLine + 10, transactions.size());
            for (Transaction transaction : transactions.subList(currentLine, endLine)) {
                System.out.println("Adresse Nom Voie: " + transaction.getAdresseNomVoie());
                transactionsRepository.save(transaction);
            }
            currentLine = endLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
