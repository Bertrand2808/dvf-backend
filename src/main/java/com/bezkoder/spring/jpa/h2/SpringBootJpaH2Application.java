package com.bezkoder.spring.jpa.h2;

import com.bezkoder.spring.jpa.h2.service.CSVReaderParcelles;
import com.bezkoder.spring.jpa.h2.service.ImportationService;
import com.bezkoder.spring.jpa.h2.service.PdfGenerateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootApplication
@EnableScheduling
public class SpringBootJpaH2Application {

//	public static void main(String[] args) {
//		try {
//			CSVReaderParcelles csvReaderParcelles = new CSVReaderParcelles();
//			csvReaderParcelles.read(new File("E:/Documents_HDD/ssd/Cours_ESGI/M1/ArchitectureLogicielle/full.csv"));
//		} catch (Exception e) {
//			e.printStackTrace(); // Handle the exception according to your requirements
//		}
//		SpringApplication.run(SpringBootJpaH2Application.class, args);
//	}
	@Autowired
	private ImportationService importationService;
		@Autowired
		private PdfGenerateurService pdfGeneratorService;

		public static void main(String[] args) {
			SpringApplication.run(SpringBootJpaH2Application.class, args);
		}

		@EventListener(ApplicationReadyEvent.class)
		public void runAfterStartup() throws FileNotFoundException {
			System.out.println("Serveur run on : " + "http://localhost:8080");
//			importationService.importerDonnees();
			// Dans une méthode appropriée
//			pdfGeneratorService.genererRapportTransactions(); // commenter le temps de tester la méthode planifiée d'importation

		}

}
