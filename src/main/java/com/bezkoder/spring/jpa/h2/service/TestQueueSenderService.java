package com.bezkoder.spring.jpa.h2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TestQueueSenderService {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public TestQueueSenderService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Scheduled(fixedRate = 600000) // Exemple de planification
    public void sendTestMessages() {
        for (int i = 0; i < 5; i++) { // Envoyer 5 messages rapidement
            String testMessage = "Latitude : 48.8566, Longitude : 2.3522, Rayon : 10000, Index: " + i;
            jmsTemplate.convertAndSend("pdfQueue", testMessage);
            System.out.println("Test message sent to the queue: " + testMessage);
        }
    }


}

