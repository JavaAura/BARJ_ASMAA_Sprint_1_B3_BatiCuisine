package com.baticuisine.ui;

import com.baticuisine.models.Client;
import com.baticuisine.services.ClientService;

public class ConsoleUi {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();

        Client client = new Client("asmaa barj", "safi ", "0123456789", false);

        clientService.ajouterClient(client);

    }
}
