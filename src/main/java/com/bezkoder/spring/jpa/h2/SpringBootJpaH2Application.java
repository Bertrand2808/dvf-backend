package com.bezkoder.spring.jpa.h2;

import com.bezkoder.spring.jpa.h2.service.ImportationService;
import com.bezkoder.spring.jpa.h2.service.PdfGenerateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class SpringBootJpaH2Application {
		Logger logger = Logger.getLogger(getClass().getName());
		final ImportationService importationService;
		final PdfGenerateurService pdfGenerateurService;
		@Autowired
		public SpringBootJpaH2Application(ImportationService importationService, PdfGenerateurService pdfGenerateurService) {
			this.importationService = importationService;
			this.pdfGenerateurService = pdfGenerateurService;
		}

		public static void main(String[] args) {
			SpringApplication.run(SpringBootJpaH2Application.class, args);
		}

		@EventListener(ApplicationReadyEvent.class)
		public void runAfterStartup() throws FileNotFoundException {
			logger.info("Application started ... launching importation");
//			importationService.importerDonnees();
			// Dans une méthode appropriée
//			pdfGeneratorService.genererRapportTransactions(); // commenter le temps de tester la méthode planifiée d'importation

		}

}
