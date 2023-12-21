package com.bezkoder.spring.jpa.h2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parcelles")
public class Parcelle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id_mutation;
    @Column(name = "date_mutation")
    private String date_mutation;
    @Column(name = "numero_disposition")
    private String numero_disposition;
    @Column(name = "nature_mutation")
    private String nature_mutation;
    @Column(name = "valeur_fonciere")
    private int valeur_fonciere;
    @Column(name = "adresse_numero")
    private String adresse_numero;
    @Column(name = "adresse_nom_voie")
    private String adresse_nom_voie;
    @Column(name = "code_postal")
    private String code_postal;
    @Column(name = "nom_commune")
    private String nom_commune;
    @Column(name = "nombre_pieces_principales")
    private int nombre_pieces_principales;
    @Column(name = "nature_culture")
    private String nature_culture;
    @Column(name = "surface_terrain")
    private double surface_terrain;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "latitude")
    private double latitude;
    public Parcelle(String id_mutation, String date_mutation, String numero_disposition, String nature_mutation, int valeur_fonciere, String adresse_numero, String adresse_nom_voie, String code_postal, String nom_commune, int nombre_pieces_principales, String nature_culture, double surface_terrain, double longitude, double latitude) {
        this.id_mutation = id_mutation;
        this.date_mutation = date_mutation;
        this.numero_disposition = numero_disposition;
        this.nature_mutation = nature_mutation;
        this.valeur_fonciere = valeur_fonciere;
        this.adresse_numero = adresse_numero;
        this.adresse_nom_voie = adresse_nom_voie;
        this.code_postal = code_postal;
        this.nom_commune = nom_commune;
        this.nombre_pieces_principales = nombre_pieces_principales;
        this.nature_culture = nature_culture;
        this.surface_terrain = surface_terrain;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId_mutation() {
        return id_mutation;
    }

    public void setId_mutation(String id_mutation) {
        this.id_mutation = id_mutation;
    }

    public String getDate_mutation() {
        return date_mutation;
    }

    public void setDate_mutation(String date_mutation) {
        this.date_mutation = date_mutation;
    }

    public String getNumero_disposition() {
        return numero_disposition;
    }

    public void setNumero_disposition(String numero_disposition) {
        this.numero_disposition = numero_disposition;
    }

    public String getNature_mutation() {
        return nature_mutation;
    }

    public void setNature_mutation(String nature_mutation) {
        this.nature_mutation = nature_mutation;
    }

    public int getValeur_fonciere() {
        return valeur_fonciere;
    }

    public void setValeur_fonciere(int valeur_fonciere) {
        this.valeur_fonciere = valeur_fonciere;
    }

    public String getAdresse_numero() {
        return adresse_numero;
    }

    public void setAdresse_numero(String adresse_numero) {
        this.adresse_numero = adresse_numero;
    }

    public String getAdresse_nom_voie() {
        return adresse_nom_voie;
    }

    public void setAdresse_nom_voie(String adresse_nom_voie) {
        this.adresse_nom_voie = adresse_nom_voie;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getNom_commune() {
        return nom_commune;
    }

    public void setNom_commune(String nom_commune) {
        this.nom_commune = nom_commune;
    }

    public int getNombre_pieces_principales() {
        return nombre_pieces_principales;
    }

    public void setNombre_pieces_principales(int nombre_pieces_principales) {
        this.nombre_pieces_principales = nombre_pieces_principales;
    }

    public String getNature_culture() {
        return nature_culture;
    }

    public void setNature_culture(String nature_culture) {
        this.nature_culture = nature_culture;
    }

    public double getSurface_terrain() {
        return surface_terrain;
    }

    public void setSurface_terrain(double surface_terrain) {
        this.surface_terrain = surface_terrain;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Parcelle{" +
                "id_mutation='" + id_mutation + '\'' +
                ", date_mutation='" + date_mutation + '\'' +
                ", numero_disposition='" + numero_disposition + '\'' +
                ", nature_mutation='" + nature_mutation + '\'' +
                ", valeur_fonciere=" + valeur_fonciere +
                ", adresse_numero='" + adresse_numero + '\'' +
                ", adresse_nom_voie='" + adresse_nom_voie + '\'' +
                ", code_postal='" + code_postal + '\'' +
                ", nom_commune='" + nom_commune + '\'' +
                ", nombre_pieces_principales=" + nombre_pieces_principales +
                ", nature_culture='" + nature_culture + '\'' +
                ", surface_terrain=" + surface_terrain +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
