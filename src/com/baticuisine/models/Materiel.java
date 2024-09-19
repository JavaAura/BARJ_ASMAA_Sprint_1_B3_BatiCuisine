package com.baticuisine.models;

public class Materiel extends Composant {
    private double quantite;
    private double coutUnitaire;

    public Materiel(int id, String nom, double coutUnitaire, double quantite, double tauxTVA, double coutTransport, double coefficientQualite) {
        super(id, nom, coutUnitaire, quantite, tauxTVA, coutTransport, coefficientQualite);
        this.quantite = quantite;
        this.coutUnitaire = coutUnitaire;
    }

    // Getters et setters
    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(double coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }
}
