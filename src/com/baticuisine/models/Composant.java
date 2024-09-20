package com.baticuisine.models;

public abstract class Composant {
    protected int id;
    protected String nom;
    protected String type;  // Correction : 'string' doit être 'String'
    protected double tauxTVA;

    public Composant(int id, String nom, String type, double tauxTVA) {  // Correction : 'string' remplacé par 'String'
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.tauxTVA = tauxTVA;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTauxTVA() {
        return tauxTVA;
    }

    public void setTauxTVA(double tauxTVA) {
        this.tauxTVA = tauxTVA;
    }
}
