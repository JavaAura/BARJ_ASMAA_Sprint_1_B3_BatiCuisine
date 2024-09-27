package com.baticuisine.repositories.interfaces;

import com.baticuisine.models.Projet;
import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface ProjetRepository {
    void ajouterProjet(Projet projet);
    void mettreAJourCoutTotal(Projet projet);
    void mettreAJourMargeBeneficiaire(int projetId, double nouvelleMarge);
    void mettreAJourEtatProjet(Projet projet);
    Optional<Projet> findByName(String nomProjet);
    Optional<Projet> findByIdWithDetails(int projetId); // Ensure this line is present
}
