package com.bezkoder.spring.jpa.h2.service;
import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class ImportationService {
    @Autowired
    private TransactionRepository transactionsRepository;

    public void importerDonnees() {
        String cheminFichier = "E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/full.csv";
        try (FileReader fileReader = new FileReader(cheminFichier)) {
            List<Transaction> transactions = new CsvToBeanBuilder(fileReader)
                    .withType(Transaction.class)
                    .build()
                    .parse();

            for (int i = 0; i < 10; i++) {
                Transaction transaction = transactions.get(i);
                System.out.println("ID Mutation: " + transaction.getIdMutation());
                System.out.println("Date de Mutation: " + transaction.getDateMutation());
                System.out.println("Numéro de Disposition: " + transaction.getNumeroDisposition());
                System.out.println("Nature de Mutation: " + transaction.getNatureMutation());
                System.out.println("Valeur Foncière: " + transaction.getValeurFonciere());
                System.out.println("Adresse Numéro: " + transaction.getAdresseNumero());
                System.out.println("Adresse Nom Voie: " + transaction.getAdresseNomVoie());
                System.out.println("Code Postal: " + transaction.getCodePostal());
                System.out.println("Nom de la Commune: " + transaction.getNomCommune());
                System.out.println("Nombre de Pièces Principales: " + transaction.getNombrePiecesPrincipales());
                System.out.println("Nature de Culture: " + transaction.getNatureCulture());
                System.out.println("Surface du Terrain: " + transaction.getSurfaceTerrain());
                System.out.println("Longitude: " + transaction.getLongitude());
                System.out.println("Latitude: " + transaction.getLatitude());
                System.out.println("--------------------------------------");

                // save transaction
                transactionsRepository.save(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
