package com.example.baptiste.smartcity.objects;

import android.location.Location;

import java.util.Date;

/**
 * Created by ordinateur on 23/05/2018.
 */

public class Annonce {
    private String nom;
    private String description;
    private String date_debut;
    private String date_fin;
    private String lieu;
    private String prix;

    public Annonce() {}

    public Annonce(String nom, String description, String date_debut, String date_fin, String lieu, String prix) {
        this.nom = nom;
        this.description = description;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.lieu = lieu;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}
