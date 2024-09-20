package com.baticuisine.ui;

import com.baticuisine.models.Client;
import com.baticuisine.models.Projet;
import com.baticuisine.models.Projet.EtatProjet;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjetService;

import java.util.Scanner;

public class ConsoleUi {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ProjetService projetService = new ProjetService();

        System.out.println("=== Ajout d'un projet ===");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int option = Integer.parseInt(scanner.nextLine());

        Client client;
        if (option == 1) {
            // Recherche d'un client existant
            client = chercherClientExistant(clientService);
        } else {
            // Ajout d'un nouveau client
            client = ajouterNouveauClient(clientService);
        }

        if (client != null) {
            // Si le client est trouvé ou ajouté, on peut ajouter le projet
            ajouterProjet(projetService, client);
        } else {
            System.out.println("Impossible d'ajouter un projet sans client.");
        }
    }

    // Méthode pour rechercher un client existant
    private static Client chercherClientExistant(ClientService clientService) {
        System.out.print("Entrez le nom du client : ");
        String nomClient = scanner.nextLine();

        Client client = clientService.chercherClientParNom(nomClient);
        if (client != null) {
            System.out.println("Client trouvé !");
            System.out.println("Nom : " + client.getNom());
            System.out.println("Adresse : " + client.getAdresse());
            System.out.println("Numéro de téléphone : " + client.getTelephone());
            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            String choix = scanner.nextLine();
            if (choix.equalsIgnoreCase("y")) {
                return client;
            }
        } else {
            System.out.println("Client non trouvé.");
        }
        return null;
    }

    // Méthode pour ajouter un nouveau client
    private static Client ajouterNouveauClient(ClientService clientService) {
        System.out.println("--- Ajout d'un nouveau client ---");
        System.out.print("Entrez le nom du client : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez l'adresse du client : ");
        String adresse = scanner.nextLine();
        System.out.print("Entrez le numéro de téléphone du client : ");
        String telephone = scanner.nextLine();
        System.out.print("Le client est-il professionnel ? (true/false) : ");
        boolean estProfessionnel = Boolean.parseBoolean(scanner.nextLine());

        Client client = new Client(nom, adresse, telephone, estProfessionnel);
        clientService.ajouterClient(client);
        System.out.println("Client ajouté avec succès !");
        return client;
    }

    // Méthode pour ajouter un projet associé à un client
    private static void ajouterProjet(ProjetService projetService, Client client) {
        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surface = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrez la marge bénéficiaire (%) : ");
        double margeBeneficiaire = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrez le coût total estimé (€) : ");
        double coutTotal = Double.parseDouble(scanner.nextLine());

        Projet projet = new Projet(0, nomProjet, surface, margeBeneficiaire, coutTotal, EtatProjet.ENCOURS, client);
        projetService.ajouterProjet(projet);

        System.out.println("Projet ajouté avec succès pour le client : " + client.getNom());
    }
}
