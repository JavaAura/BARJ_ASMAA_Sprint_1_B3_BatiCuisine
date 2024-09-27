package com.baticuisine.models;

import java.util.ArrayList;
import java.util.List;

public class Projet {
    private int id;
    private String nomProjet;
    private double surface;
    private Double margeBeneficiaire;
    private double coutTotal;
    private EtatProjet etatProjet;
    private Client client;
    private Devis devis;
    private final List<Composant> composants = new ArrayList<>();

    public enum EtatProjet {
        ENCOURS, TERMINE, ANNULE
    }

    public Projet(String nomProjet, double surface, Double margeBeneficiaire, EtatProjet etatProjet, Client client) {
        this.nomProjet = nomProjet;
        this.surface = surface;
        this.margeBeneficiaire = margeBeneficiaire;
        this.etatProjet = etatProjet;
        this.client = client;
        this.coutTotal = 0;
    }

    public void ajouterComposant(Composant composant) {
        composants.add(composant);
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public Double getMargeBeneficiaire() {
        return margeBeneficiaire;
    }

    public void setMargeBeneficiaire(Double margeBeneficiaire) {
        this.margeBeneficiaire = margeBeneficiaire;
    }

    public double getCoutTotal() {
        return coutTotal;
    }
    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }
    public EtatProjet getEtatProjet() {
        return etatProjet;
    }

    public void setEtatProjet(EtatProjet etatProjet) {
        this.etatProjet = etatProjet;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Composant> getComposants() {
        return composants;
    }
    public void setComposants(List<Composant> composants) {
        this.composants.clear();
        this.composants.addAll(composants);
    }

}
