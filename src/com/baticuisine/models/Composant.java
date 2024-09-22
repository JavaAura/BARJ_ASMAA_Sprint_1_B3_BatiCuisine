package com.baticuisine.models;

public abstract class Composant {
    protected int id;
    protected String nom;
    protected String type;
    protected Double tauxTVA;
    protected int projetId;

    public Composant(int id, String nom, String type, Double tauxTVA, int projetId) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.tauxTVA = tauxTVA;
        this.projetId = projetId;

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

    public Double getTauxTVA() {
        return tauxTVA;
    }

    public void setTauxTVA(Double tauxTVA) {
        this.tauxTVA = tauxTVA;
    }
    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }
}
