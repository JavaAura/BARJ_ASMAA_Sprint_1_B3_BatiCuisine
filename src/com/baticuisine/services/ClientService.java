package com.baticuisine.services;

import com.baticuisine.models.Client;
import com.baticuisine.repositories.interfaces.ClientRepository;
import com.baticuisine.repositories.impl.ClientRepositoryImpl;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepositoryImpl();
    }

    public void ajouterClient(Client client) {
        clientRepository.ajouterClient(client);
    }

    public Client chercherClientParNom(String nom) {
        return clientRepository.chercherClientParNom(nom);
    }
}
