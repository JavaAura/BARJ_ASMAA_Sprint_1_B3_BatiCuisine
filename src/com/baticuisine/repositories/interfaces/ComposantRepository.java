package com.baticuisine.repositories.interfaces;

import com.baticuisine.models.Composant;
import java.util.List;


public interface ComposantRepository {
    void ajouterComposant(Composant composant);
    void mettreAJourTauxTVA(int projetId, double nouveauTauxTVA);
}
