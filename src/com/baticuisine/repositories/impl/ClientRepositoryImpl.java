package com.baticuisine.repositories.impl;

import com.baticuisine.models.Client;
import com.baticuisine.repositories.interfaces.ClientRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {
    private List<Client> clients = new ArrayList<>();

    @Override
    public void ajouterClient(Client client) {
        clients.add(client);
        System.out.println("Client ajouté : " + client);
    }

    @Override
    public Client rechercherClientParNom(String nom) {
        for (Client client : clients) {
            if (client.getNom().equalsIgnoreCase(nom)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> getAllClients() {
        return clients;
    }
}
