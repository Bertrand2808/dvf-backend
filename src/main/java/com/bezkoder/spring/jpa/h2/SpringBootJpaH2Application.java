package com.bezkoder.spring.jpa.h2;
import jakarta.jms.ConnectionFactory;
import com.bezkoder.spring.jpa.h2.service.ImportationService;
import com.bezkoder.spring.jpa.h2.service.PdfGenerateurService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.FileNotFoundException;
import java.util.logging.Logger;




@SpringBootApplication
@EnableScheduling
@AllArgsConstructor
@EnableJms
public class SpringBootJpaH2Application {
	final ImportationService importationService;

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all auto-configured defaults to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some settings if necessary.
		return factory;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootJpaH2Application.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() throws FileNotFoundException {
		System.out.println("Application started ... launching importation");
//			importationService.importerDonnees();
		// Dans une méthode appropriée
//			pdfGeneratorService.genererRapportTransactions(); // commenter le temps de tester la méthode planifiée d'importation

	}

}