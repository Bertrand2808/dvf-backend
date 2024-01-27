package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.config.MyWebSocketHandler;
import com.bezkoder.spring.jpa.h2.dto.RechercherDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Base64;

@Component
public class PdfReceiver {

    private final PdfGenerateurService pdfGenerateurService;
    private final MyWebSocketHandler myWebSocketHandler;

    @Autowired
    public PdfReceiver(PdfGenerateurService pdfGenerateurService, MyWebSocketHandler myWebSocketHandler) {
        this.pdfGenerateurService = pdfGenerateurService;
        this.myWebSocketHandler = myWebSocketHandler;
    }
    @JmsListener(destination = "pdfQueue", containerFactory = "myFactory")
    public void receivePdf(String message) throws IOException {
        ResponseEntity<byte[]> responseEntity = null;
        System.out.println("Received <" + message + ">");
        double latitude = extractValue(message, "Latitude");
        double longitude = extractValue(message, "Longitude");
        double rayon = extractValue(message, "Rayon");
        System.out.println("attributs : " + latitude + ", " + longitude + ", " + rayon);
        String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";
        String path = "src/main/resources/" + fileName;
        try {
            byte [] pdfBytes = pdfGenerateurService.pdfGenerate(path, latitude, longitude, rayon);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport.pdf");
            responseEntity = ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
            myWebSocketHandler.sendPdfGeneratedNotification(base64Pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour extraire les valeurs de latitude, longitude ou rayon de la chaîne message
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
                    // Gérer l'exception en cas d'erreur de conversion
                    e.printStackTrace();
                }
            }
        }
        return 0.0; // Valeur par défaut si la valeur n'est pas trouvée
    }


}