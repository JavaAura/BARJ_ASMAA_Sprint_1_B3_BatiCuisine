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
        this.coutTotal = 0;
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

        double totalMateriauxAvecTVA = totalMateriaux;
        double totalMainOeuvreAvecTVA = totalMainOeuvre;

        for (Composant composant : composants) {
            if (composant instanceof Materiel materiel && materiel.getTauxTVA() != null) {
                double tva = materiel.getTauxTVA() / 100;
                totalMateriauxAvecTVA += totalMateriaux * tva;
            }
            if (composant instanceof MainOeuvre mainOeuvre && mainOeuvre.getTauxTVA() != null) {
                double tva = mainOeuvre.getTauxTVA() / 100;
                totalMainOeuvreAvecTVA += totalMainOeuvre * tva;
            }
        }

        this.coutTotal = totalMateriauxAvecTVA + totalMainOeuvreAvecTVA;
    }

    public void afficherResultats() {
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

        double totalMateriauxAvecTVA = totalMateriaux * 1.2;
        double totalMainOeuvreAvecTVA = totalMainOeuvre * 1.2;
        double totalAvantMarge = totalMateriauxAvecTVA + totalMainOeuvreAvecTVA;

        double margeBeneficiaire = totalAvantMarge * (this.margeBeneficiaire / 100);
        double coutTotalFinal = totalAvantMarge + margeBeneficiaire;

        System.out.println("--- Résultat du Calcul ---");
        System.out.println("Coût total des matériaux avant TVA : " + totalMateriaux + " €");
        System.out.println("Coût total des matériaux avec TVA : " + totalMateriauxAvecTVA + " €");
        System.out.println("Coût total de la main-d'œuvre avant TVA : " + totalMainOeuvre + " €");
        System.out.println("Coût total de la main-d'œuvre avec TVA : " + totalMainOeuvreAvecTVA + " €");
        System.out.println("Coût total avant marge : " + totalAvantMarge + " €");
        System.out.println("Marge bénéficiaire (" + this.margeBeneficiaire + "%) : " + margeBeneficiaire + " €");
        System.out.println("Coût total final du projet : " + coutTotalFinal + " €");
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