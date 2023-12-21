package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.model.Parcelle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ParcelleController {

    private final List<Parcelle> parcelleList = new ArrayList<>();

    @GetMapping("/parcelles")
    public ResponseEntity<List<Parcelle>> getAllParcelles() {
        return new ResponseEntity<>(parcelleList, HttpStatus.OK);
    }

    @GetMapping("/parcelles/{id}")
    public ResponseEntity<Parcelle> getParcelleById(@PathVariable("id") String id) {
        Parcelle parcelle = findParcelleById(id);
        if (parcelle != null) {
            return new ResponseEntity<>(parcelle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/parcelles")
    public ResponseEntity<Parcelle> createParcelle(@RequestBody Parcelle parcelle) {
        parcelleList.add(parcelle);
        return new ResponseEntity<>(parcelle, HttpStatus.CREATED);
    }

    @PutMapping("/parcelles/{id}")
    public ResponseEntity<Parcelle> updateParcelle(@PathVariable("id") String id, @RequestBody Parcelle updatedParcelle) {
        Parcelle existingParcelle = findParcelleById(id);
        if (existingParcelle != null) {
            // Update fields based on the provided data
            existingParcelle.setDate_mutation(updatedParcelle.getDate_mutation());
            existingParcelle.setNumero_disposition(updatedParcelle.getNumero_disposition());
            // Update other fields similarly...

            return new ResponseEntity<>(existingParcelle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/parcelles/{id}")
    public ResponseEntity<HttpStatus> deleteParcelle(@PathVariable("id") String id) {
        Parcelle parcelle = findParcelleById(id);
        if (parcelle != null) {
            parcelleList.remove(parcelle);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/parcelles")
    public ResponseEntity<HttpStatus> deleteAllParcelles() {
        parcelleList.clear();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Parcelle findParcelleById(String id) {
        // Simple linear search for demonstration purposes
        for (Parcelle parcelle : parcelleList) {
            if (parcelle.getId_mutation().equals(id)) {
                return parcelle;
            }
        }
        return null;
    }
}
