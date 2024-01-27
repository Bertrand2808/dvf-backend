package com.bezkoder.spring.jpa.h2.business;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // identifiant unique pour la base de données

    // champs correspondant au fichier CSV
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

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIdMutation() {
        return idMutation;
    }
    public void setIdMutation(String idMutation) {
        this.idMutation = idMutation;
    }
    public String getDateMutation() {
        return dateMutation;
    }
    public void setDateMutation(String dateMutation) {
        this.dateMutation = dateMutation;
    }
    public Integer getNumeroDisposition() {
        return numeroDisposition;
    }
    public void setNumeroDisposition(Integer numeroDisposition) {
        this.numeroDisposition = numeroDisposition;
    }
    public String getNatureMutation() {
        return natureMutation;
    }
    public void setNatureMutation(String natureMutation) {
        this.natureMutation = natureMutation;
    }
    public Double getValeurFonciere() {
        return valeurFonciere;
    }
    public void setValeurFonciere(Double valeurFonciere) {
        this.valeurFonciere = valeurFonciere;
    }
    public Integer getAdresseNumero() {
        return adresseNumero;
    }
    public void setAdresseNumero(Integer adresseNumero) {
        this.adresseNumero = adresseNumero;
    }
    public String getAdresseNomVoie() {
        return adresseNomVoie;
    }
    public void setAdresseNomVoie(String adresseNomVoie) {
        this.adresseNomVoie = adresseNomVoie;
    }
    public String getCodePostal() {
        return codePostal;
    }
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
    public String getNomCommune() {
        return nomCommune;
    }
    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }
    public Integer getNombrePiecesPrincipales() {
        return nombrePiecesPrincipales;
    }
    public void setNombrePiecesPrincipales(Integer nombrePiecesPrincipales) {
        this.nombrePiecesPrincipales = nombrePiecesPrincipales;
    }
    public String getNatureCulture() {
        return natureCulture;
    }
    public void setNatureCulture(String natureCulture) {
        this.natureCulture = natureCulture;
    }
    public Double getSurfaceTerrain() {
        return surfaceTerrain;
    }
    public void setSurfaceTerrain(Double surfaceTerrain) {
        this.surfaceTerrain = surfaceTerrain;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
