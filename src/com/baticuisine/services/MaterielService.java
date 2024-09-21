package com.baticuisine.services;

import com.baticuisine.models.Materiel;
import com.baticuisine.repositories.interfaces.MaterielRepository;
import com.baticuisine.repositories.impl.MaterielRepositoryImpl;

public class MaterielService {
    private final MaterielRepository materielRepository;

    public MaterielService() {
        this.materielRepository = new MaterielRepositoryImpl();
    }

    public void ajouterMateriel(Materiel materiel) {
        materielRepository.ajouterMateriel(materiel);
    }
}
