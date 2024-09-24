package com.baticuisine.repositories.interfaces;

import com.baticuisine.models.Projet;

import java.util.List;

public interface ProjetRepository {
    void ajouterProjet(Projet projet);
    void mettreAJourCoutTotal(Projet projet);
    void mettreAJourMargeBeneficiaire(int projetId, double nouvelleMarge);
    void mettreAJourEtatProjet(Projet projet);
    List<Projet> recupererTousLesProjets();
}
