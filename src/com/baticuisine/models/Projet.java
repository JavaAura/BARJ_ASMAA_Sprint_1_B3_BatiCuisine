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
        this.coutTotal = 0;  // Initialize coutTotal
    }

    public void ajouterComposant(Composant composant) {
        composants.add(composant);
        calculerCoutTotal();
    }

    public void calculerCoutTotal() {
        double totalMateriaux = 0;
        double totalMainOeuvre = 0;

        for (Composant composant : composants) {
            if (composant instanceof Materiel materiel) {
                double coutMateriel = (materiel.getCoutUnitaire() * materiel.getQuantite()) + materiel.getCoutTransport();
                totalMateriaux += coutMateriel;
            } else if (composant instanceof MainOeuvre mainOeuvre) {
                double coutMainOeuvre = mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                totalMainOeuvre += coutMainOeuvre;
            }
        }

        this.coutTotal = totalMateriaux + totalMainOeuvre;
        System.out.println("Cout total calculé: " + this.coutTotal);  // Debugging statement
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
}
