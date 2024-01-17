package com.bezkoder.spring.jpa.h2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class RechercheController {
    @GetMapping ("/transactions")
    public ResponseEntity<Map<String, String>> rechercher(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "rayon") double rayon ) {

        System.out.println("Latitude : " + latitude);
        System.out.println("Longitude : " + longitude);
        System.out.println("Rayon : " + rayon);

        // TODO : rechercher les transactions dans le rayon donné
        // TODO : retourner les transactions trouvées

        // retourner les données en json
        return ResponseEntity.ok().body(Map.of("message", "OK"));
    }
}
