package com.baticuisine.services;

import com.baticuisine.models.Client;
import com.baticuisine.repositories.interfaces.ClientRepository;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void ajouterClient(Client client) {
        if (clientRepository.clientExiste(client.getNom())) {
            System.out.println("Le client avec le nom \"" + client.getNom() + "\" existe déjà et ne peut pas être ajouté.");
        } else {
            clientRepository.ajouterClient(client);
            System.out.println("Client ajouté : " + client.getNom());
        }
    }

    public Client rechercherClientParNom(String nom) {
        return clientRepository.rechercherClientParNom(nom);
    }
}
