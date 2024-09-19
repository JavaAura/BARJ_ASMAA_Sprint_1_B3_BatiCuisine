package com.baticuisine.services;

import com.baticuisine.models.Client;
import com.baticuisine.repositories.interfaces.ClientRepository;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void ajouterClient(Client client) {
        clientRepository.ajouterClient(client);
    }

    public Client rechercherClientParNom(String nom) {
        return clientRepository.rechercherClientParNom(nom);
    }
}
