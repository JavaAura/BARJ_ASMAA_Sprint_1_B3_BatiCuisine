package com.baticuisine.services;

import com.baticuisine.models.Composant;
import com.baticuisine.repositories.impl.ComposantRepositoryImpl;
import com.baticuisine.repositories.interfaces.ComposantRepository;

public class ComposantService {
    private final ComposantRepository composantRepository;

    public ComposantService() {
        this.composantRepository = new ComposantRepositoryImpl();
    }

    public void ajouterComposant(Composant composant) {
        composantRepository.ajouterComposant(composant);
    }
}
