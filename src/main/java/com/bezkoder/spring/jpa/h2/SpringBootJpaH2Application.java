package com.bezkoder.spring.jpa.h2;

import com.bezkoder.spring.jpa.h2.service.CSVReaderParcelles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class SpringBootJpaH2Application {

	public static void main(String[] args) {
		try {
			CSVReaderParcelles csvReaderParcelles = new CSVReaderParcelles();
			csvReaderParcelles.read(new File("src/main/resources/parcelles.csv"));
		} catch (Exception e) {
			e.printStackTrace(); // Handle the exception according to your requirements
		}
		SpringApplication.run(SpringBootJpaH2Application.class, args);
	}

}
