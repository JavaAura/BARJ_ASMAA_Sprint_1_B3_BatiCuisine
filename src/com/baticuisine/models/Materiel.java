package com.baticuisine.models;

public class Materiel extends Composant {
    private double quantite;
    private double coutUnitaire;
    protected double coutTransport;
    protected double coefficientQualite;

    public Materiel(int id, String nom, String type, double tauxTVA, double quantite, double coutUnitaire, double coutTransport, double coefficientQualite) {
        super(id, nom, type, tauxTVA);
        this.quantite = quantite;
        this.coutUnitaire = coutUnitaire;
        this.coutTransport = coutTransport;
        this.coefficientQualite = coefficientQualite;
    }

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

    public double getCoutTransport() {
        return coutTransport;
    }

    public void setCoutTransport(double coutTransport) {
        this.coutTransport = coutTransport;
    }

    public double getCoefficientQualite() {
        return coefficientQualite;
    }

    public void setCoefficientQualite(double coefficientQualite) {
        this.coefficientQualite = coefficientQualite;
    }
}
