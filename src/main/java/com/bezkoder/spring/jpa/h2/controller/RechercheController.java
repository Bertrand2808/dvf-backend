package com.bezkoder.spring.jpa.h2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class RechercheController {
    private static final double EARTH_RADIUS = 6371e3; // en mètres

    @GetMapping ("/transactions")
    public ResponseEntity<Map<String, Object>> rechercher(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "rayon") double rayon ) {

        System.out.println("Latitude : " + latitude);
        System.out.println("Longitude : " + longitude);
        System.out.println("Rayon : " + rayon);

        // TODO : rechercher les transactions dans le rayon donné
        List<Map<String, Double>> points = calculerPointsAutour(latitude, longitude, rayon);
        // TODO : retourner les transactions trouvées

        // retourner les données en json
//        return ResponseEntity.ok().body(Map.of("message", "OK"));
        return ResponseEntity.ok().body(Map.of("points", points));
    }

    private List<Map<String, Double>> calculerPointsAutour(double latitude, double longitude, double rayon) {
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
}

