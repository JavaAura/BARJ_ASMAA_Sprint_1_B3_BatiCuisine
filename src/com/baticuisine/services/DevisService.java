package com.baticuisine.services;

import com.baticuisine.models.Devis;
import com.baticuisine.repositories.interfaces.DevisRepository;

public class DevisService {
    private final DevisRepository devisRepository;

    public DevisService(DevisRepository devisRepository) {
        this.devisRepository = devisRepository;
    }

    public void enregistrerDevis(Devis devis) {
        devisRepository.enregistrerDevis(devis);
    }
}
