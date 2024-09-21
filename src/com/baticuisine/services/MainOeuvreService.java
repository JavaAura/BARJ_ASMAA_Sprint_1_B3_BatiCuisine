package com.baticuisine.services;

import com.baticuisine.models.MainOeuvre;
import com.baticuisine.repositories.interfaces.MainOeuvreRepository;
import com.baticuisine.repositories.impl.MainOeuvreRepositoryImpl;

public class MainOeuvreService {
    private final MainOeuvreRepository mainOeuvreRepository;

    public MainOeuvreService() {
        this.mainOeuvreRepository = new MainOeuvreRepositoryImpl();
    }

    public void ajouterMainOeuvre(MainOeuvre mainOeuvre) {
        mainOeuvreRepository.ajouterMainOeuvre(mainOeuvre);
    }
}
