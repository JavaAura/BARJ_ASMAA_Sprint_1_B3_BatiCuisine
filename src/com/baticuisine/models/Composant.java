package com.baticuisine.models;

public abstract class Composant {
    protected int id;
    protected String nom;
    protected double coutUnitaire;
    protected double quantite;
    protected double tauxTVA;
    protected double coutTransport;
    protected double coefficientQualite;

    public Composant(int id, String nom, double coutUnitaire, double quantite, double tauxTVA, double coutTransport, double coefficientQualite) {
        this.id = id;
        this.nom = nom;
        this.coutUnitaire = coutUnitaire;
        this.quantite = quantite;
        this.tauxTVA = tauxTVA;
        this.coutTransport = coutTransport;
        this.coefficientQualite = coefficientQualite;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(double coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getTauxTVA() {
        return tauxTVA;
    }

    public void setTauxTVA(double tauxTVA) {
        this.tauxTVA = tauxTVA;
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
