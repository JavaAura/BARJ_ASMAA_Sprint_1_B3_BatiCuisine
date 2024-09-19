package com.baticuisine.repositories.interfaces;

import com.baticuisine.models.Client;

import java.util.List;

public interface ClientRepository {
    void ajouterClient(Client client);
    Client rechercherClientParNom(String nom);
    List<Client> getAllClients();

    boolean clientExiste(String nom);
}
