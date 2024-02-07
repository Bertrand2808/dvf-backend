package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.config.MyWebSocketHandler;
import com.bezkoder.spring.jpa.h2.exception.PdfGenerationException;
import com.bezkoder.spring.jpa.h2.exception.ValueExtractionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PdfReceiver {
    private final Logger logger = Logger.getLogger(PdfReceiver.class.getName());
    private final PdfGenerateurService pdfGenerateurService;
    private final MyWebSocketHandler myWebSocketHandler;
    @Autowired
    public PdfReceiver(PdfGenerateurService pdfGenerateurService, MyWebSocketHandler myWebSocketHandler) {
        this.pdfGenerateurService = pdfGenerateurService;
        this.myWebSocketHandler = myWebSocketHandler;
    }
    /**
     * Méthode pour recevoir les messages de la file pdfQueue
     * @param message
     * @throws IOException
     */
    @JmsListener(destination = "pdfQueue", containerFactory = "myFactory")
    public void receivePdf(String message) throws IOException {
        if(logger.isLoggable(Level.INFO)) {
            logger.info("Received <" + message + ">");
        }
        double latitude = extractValue(message, "Latitude");
        double longitude = extractValue(message, "Longitude");
        double rayon = extractValue(message, "Rayon");
        if(logger.isLoggable(Level.INFO)) {
            logger.info("Latitude : " + latitude);
            logger.info("Longitude : " + longitude);
            logger.info("Rayon : " + rayon);
        }
        String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";
        String path = "src/main/resources/" + fileName;
        pdfGenerateurService.enqueuePdfGeneration(path, latitude, longitude, rayon, (pdfBytes) -> {
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
            myWebSocketHandler.sendPdfGeneratedNotification(base64Pdf);
        });
    }

    /**
     * Méthode pour extraire la valeur d'un attribut dans un message
     * @param message
     * @param key
     * @return
     */
    private double extractValue(String message, String key) {
        String startTag = key + " : ";
        int startIndex = message.indexOf(startTag);
        if (startIndex != -1) {
            int endIndex = message.indexOf(',', startIndex);
            if (endIndex != -1) {
                String valueStr = message.substring(startIndex + startTag.length(), endIndex);
                try {
                    return Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    throw new ValueExtractionException("Erreur lors de l'extraction de la valeur.", e);
                }
            }
        }
        return 0.0;
    }


}