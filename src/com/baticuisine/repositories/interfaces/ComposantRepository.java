package com.baticuisine.repositories.interfaces;

import com.baticuisine.models.Composant;

public interface ComposantRepository {
    void ajouterComposant(Composant composant);
    void mettreAJourTauxTVA(int projetId, double nouveauTauxTVA);
}
