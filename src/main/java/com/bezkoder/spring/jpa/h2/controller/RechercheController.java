package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class RechercheController {
    private final TransactionRepository transactionRepository;
    @Autowired
    public RechercheController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    private static final double EARTH_RADIUS = 6371e3; // en mètres

    @GetMapping ("/transactions")
    public ResponseEntity<List<Transaction>> rechercher(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "rayon") double rayon ) {

        System.out.println("Latitude : " + latitude);
        System.out.println("Longitude : " + longitude);
        System.out.println("Rayon : " + rayon);

//        List<Map<String, Double>> points = calculerTransactions(latitude, longitude, rayon);
//        return ResponseEntity.ok().body(Map.of("points", points));

        List<Transaction> allTransactions = transactionRepository.findAll();
        List<Transaction> transactionsDansRayon = allTransactions.stream()
                .filter(t -> calculerDistance(latitude, longitude, t.getLatitude(), t.getLongitude()) <= rayon)
                .collect(Collectors.toList());
        System.out.println("Nombre de transactions dans le rayon : " + transactionsDansRayon.size());
        return ResponseEntity.ok().body(transactionsDansRayon);
    }

    private List<Map<String, Double>> calculerTransactions(double latitude, double longitude, double rayon) {
        List<Map<String, Double>> points = new ArrayList<>();
        double latRadians = Math.toRadians(latitude);
        double lonRadians = Math.toRadians(longitude);

        // Calculer les points sur un cercle autour du rayon donné
        for (int angle = 0; angle < 360; angle++) {
            double angleRadians = Math.toRadians(angle);
            double latPoint = Math.asin(Math.sin(latRadians) * Math.cos(rayon / EARTH_RADIUS) +
                    Math.cos(latRadians) * Math.sin(rayon / EARTH_RADIUS) * Math.cos(angleRadians));
            double lonPoint = lonRadians + Math.atan2(Math.sin(angleRadians) * Math.sin(rayon / EARTH_RADIUS) * Math.cos(latRadians),
                    Math.cos(rayon / EARTH_RADIUS) - Math.sin(latRadians) * Math.sin(latPoint));
            latPoint = Math.toDegrees(latPoint);
            lonPoint = Math.toDegrees(lonPoint);

            points.add(Map.of("latitude", latPoint, "longitude", lonPoint));
        }
        return points;
    }

    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        System.out.println("Distance : " + EARTH_RADIUS * c);
        return EARTH_RADIUS * c; // Distance en mètres
    }
}

