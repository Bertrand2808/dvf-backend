package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.dto.RechercherDTO;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RechercheController {
    @PostMapping("/transactions")
    public String rechercher(@RequestBody RechercherDTO recherche) {
        System.out.println("Latitude : " + recherche.getLatitude());
        System.out.println("Longitude : " + recherche.getLongitude());
        System.out.println("Rayon : " + recherche.getRayon());

        // TODO : rechercher les transactions dans le rayon donné
        // TODO : retourner les transactions trouvées

        return "OK";
    }
}
