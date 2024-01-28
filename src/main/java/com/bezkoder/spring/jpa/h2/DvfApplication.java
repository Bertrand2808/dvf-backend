package com.bezkoder.spring.jpa.h2;
import jakarta.jms.ConnectionFactory;
import com.bezkoder.spring.jpa.h2.service.ImportationService;
import lombok.AllArgsConstructor;
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

import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
@AllArgsConstructor
@EnableJms
public class DvfApplication {
	final ImportationService importationService;

	private final Logger logger = Logger.getLogger(DvfApplication.class.getName());
	@Bean
	public JmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DvfApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup(){
		if(logger.isLoggable(java.util.logging.Level.INFO)) {
			logger.info("Démarrage de l'application, importation des données en cours...");
		}
	}
}