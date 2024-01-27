package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.model.Parcelle;

import java.io.File;
import java.io.FileReader;

import com.bezkoder.spring.jpa.h2.repository.ParcelleRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;


public class CSVReaderParcelles {

    public static void main(String[] args) {
        try {
            CSVReaderParcelles csvReaderParcelles = new CSVReaderParcelles();
            csvReaderParcelles.read(new File("src/main/resources/parcelles.csv"));
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your requirements
        }
    }
    public void read(File file) throws Exception {
       // if (parcelleService.isParcelleEmpty()) {
            String [] record;
            CSVReader csvReader = null;

            try {
                csvReader = new CSVReaderBuilder(new FileReader(file))
                        .withCSVParser(new CSVParserBuilder()
                                .withSeparator(',')
                                .build())
                        .build();

                int currentLine = 0;

                while ((record = csvReader.readNext()) != null) {

//                    System.out.println(
//                            String.format(
//                                    "Line %d Col1: %s Col2: %s Col3: %s",
//                                    currentLine,
//                                    record[0],
//                                    record[1],
//                                    record[2]
//                            )
//                    );


                    currentLine++;
                }
            } finally {
                //Close the reader
                if (csvReader != null) {
                    csvReader.close();
                }
            }

       /* } else {
            System.out.println("Parcelle entity is not empty. Skipping data import.");
        }*/
    }
}
