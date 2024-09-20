package com.baticuisine.repositories.interfaces;

import com.baticuisine.models.Client;

public interface ClientRepository {

    void ajouterClient(Client client);

    Client chercherClientParNom(String nom);

    boolean clientExists(String nom);
}
