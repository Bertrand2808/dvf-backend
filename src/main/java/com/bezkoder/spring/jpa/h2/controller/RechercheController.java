package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.exception.ParametresManquantsException;
import com.bezkoder.spring.jpa.h2.service.JmsMessageSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RechercheController {
    private final Logger logger = Logger.getLogger(RechercheController.class.getName());
    private final JmsMessageSender jmsMessageSender;

    /**
     * GET /transactions : get transactions depending on the given latitude, longitude and radius.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @Operation(summary = "Retrieve transactions based on latitude, longitude, and radius", tags = { "transactions"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions found", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json") })
    })
    @GetMapping ("/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> rechercherTransaction(
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "rayon") Double rayon ) {
        if (latitude == null || longitude == null || rayon == null) {
            throw new ParametresManquantsException("Les paramètres latitude, longitude et rayon sont obligatoires.");
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info(MessageFormat.format("Recherche de transactions avec latitude : {0}, longitude : {1}, rayon : {2}", latitude, longitude, rayon));
        }
        String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";
        String path = "src/main/resources/" + fileName;
        String message = "Générer PDF pour Latitude : " + latitude + ", Longitude : " + longitude + ", Rayon : " + rayon + ", path : " + path;
        jmsMessageSender.send("pdfQueue", message);
        return ResponseEntity.ok("Recherche en cours, veuillez attendre la notification.");
    }
}
