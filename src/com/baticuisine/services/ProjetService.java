package com.baticuisine.services;

import com.baticuisine.models.Projet;
import com.baticuisine.repositories.interfaces.ProjetRepository;
import com.baticuisine.repositories.impl.ProjetRepositoryImpl;

public class ProjetService {
    private final ProjetRepository projetRepository;

    public ProjetService() {
        this.projetRepository = new ProjetRepositoryImpl();
    }

    public void ajouterProjet(Projet projet) {
        projetRepository.ajouterProjet(projet);
    }
    public void mettreAJourCoutTotal(Projet projet) {
        projetRepository.mettreAJourCoutTotal(projet);
    }
}
