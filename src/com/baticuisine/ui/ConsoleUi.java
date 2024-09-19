package com.baticuisine.ui;

import com.baticuisine.models.Client;
import com.baticuisine.repositories.impl.ClientRepositoryImpl;
import com.baticuisine.services.ClientService;

import java.util.Scanner;

public class ConsoleUi {

    public static void main(String[] args) {
        ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
        ClientService clientService = new ClientService(clientRepository);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Rechercher un client");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    System.out.print("Entrez le nom du client : ");
                    String nom = scanner.nextLine();
                    System.out.print("Entrez l'adresse du client : ");
                    String adresse = scanner.nextLine();
                    System.out.print("Entrez le téléphone du client : ");
                    String telephone = scanner.nextLine();
                    System.out.print("Le client est-il un professionnel ? (true/false) : ");
                    boolean estProfessionnel = scanner.nextBoolean();

                    Client client = new Client(0, nom, adresse, telephone, estProfessionnel);
                    clientService.ajouterClient(client);
                    break;

                case 2:
                    System.out.print("Entrez le nom du client à rechercher : ");
                    String nomRecherche = scanner.nextLine();
                    Client clientTrouve = clientService.rechercherClientParNom(nomRecherche);

                    if (clientTrouve != null) {
                        System.out.println("Client trouvé : " + clientTrouve);
                    } else {
                        System.out.println("Client non trouvé !");
                    }
                    break;

                case 3:
                    System.out.println("Au revoir !");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }
}
