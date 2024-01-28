package com.bezkoder.spring.jpa.h2.business;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Transaction {
    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CsvBindByName(column = "id_mutation")
    private String idMutation;
    @CsvBindByName(column = "date_mutation")
    private String dateMutation;
    @CsvBindByName(column = "numero_disposition")
    private Integer numeroDisposition;
    @CsvBindByName(column = "nature_mutation")
    private String natureMutation;
    @CsvBindByName(column = "valeur_fonciere")
    private Double valeurFonciere;
    @CsvBindByName(column = "adresse_numero")
    private Integer adresseNumero;
    @CsvBindByName(column = "adresse_nom_voie")
    private String adresseNomVoie;
    @CsvBindByName(column = "code_postal")
    private String codePostal;
    @CsvBindByName(column = "nom_commune")
    private String nomCommune;
    @CsvBindByName(column = "nombre_pieces_principales")
    private Integer nombrePiecesPrincipales;
    @CsvBindByName(column = "nature_culture")
    private String natureCulture;
    @CsvBindByName(column = "surface_terrain")
    private Double surfaceTerrain;
    @CsvBindByName(column = "longitude")
    private Double longitude;
    @CsvBindByName(column = "latitude")
    private Double latitude;

}
