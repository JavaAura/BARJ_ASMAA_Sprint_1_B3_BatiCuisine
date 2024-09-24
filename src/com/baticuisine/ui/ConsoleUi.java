package com.baticuisine.ui;

import com.baticuisine.models.Client;
import com.baticuisine.models.Projet;
import com.baticuisine.models.Projet.EtatProjet;
import com.baticuisine.models.Materiel;
import com.baticuisine.models.MainOeuvre;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjetService;
import com.baticuisine.services.ComposantService;
import com.baticuisine.repositories.impl.ComposantRepositoryImpl;
import com.baticuisine.repositories.impl.ProjetRepositoryImpl;

import java.util.Scanner;

public class ConsoleUi {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ProjetService projetService = new ProjetService();
        ComposantService composantService = new ComposantService();

        System.out.println("=== Ajout d'un projet ===");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");

        int option = Integer.parseInt(scanner.nextLine());

        Client client;
        if (option == 1) {
            client = chercherClientExistant(clientService);
        } else {
            client = ajouterNouveauClient(clientService);
        }

        if (client != null) {
            Projet projet = ajouterProjet(projetService, client);
            ajouterComposants(projet, composantService);
            projetService.mettreAJourCoutTotal(projet);
            appliquerTVA(projet);
            appliquerMargeBeneficiaire(projet); // New margin function
        } else {
            System.out.println("Impossible d'ajouter un projet sans client.");
        }
    }

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

    private static Projet ajouterProjet(ProjetService projetService, Client client) {
        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surface = Double.parseDouble(scanner.nextLine());

        Double margeBeneficiaire = null; // Optional

        Projet projet = new Projet(nomProjet, surface, margeBeneficiaire, EtatProjet.ENCOURS, client);
        projetService.ajouterProjet(projet);
        System.out.println("Projet ajouté avec succès pour le client : " + client.getNom());
        return projet;
    }

    private static void ajouterComposants(Projet projet, ComposantService composantService) {
        while (true) {
            System.out.println("--- Ajout des matériaux ---");
            System.out.print("Entrez le nom du matériau : ");
            String nomMateriel = scanner.nextLine();
            System.out.print("Entrez la quantité de ce matériau : ");
            double quantite = Double.parseDouble(scanner.nextLine());
            System.out.print("Entrez le coût unitaire de ce matériau : ");
            double coutUnitaire = Double.parseDouble(scanner.nextLine());
            System.out.print("Entrez le coût de transport de ce matériau : ");
            double coutTransport = Double.parseDouble(scanner.nextLine());
            System.out.print("Entrez le coefficient de qualité du matériau : ");
            double coefficientQualite = Double.parseDouble(scanner.nextLine());

            // Creating the material object with default tauxTVA of 0.0
            Materiel materiel = new Materiel(0, nomMateriel, "Matériel", 0.0, projet.getId(), quantite, coutUnitaire, coutTransport, coefficientQualite);
            projet.ajouterComposant(materiel);
            composantService.ajouterComposant(materiel);
            System.out.println("Matériau ajouté avec succès !");

            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            if (scanner.nextLine().equalsIgnoreCase("n")) {
                break;
            }
        }

        while (true) {
            System.out.println("--- Ajout de la main-d'œuvre ---");
            System.out.print("Entrez le type de main-d'œuvre : ");
            String nomMainOeuvre = scanner.nextLine();
            System.out.print("Entrez le taux horaire de cette main-d'œuvre : ");
            double tauxHoraire = Double.parseDouble(scanner.nextLine());
            System.out.print("Entrez le nombre d'heures travaillées : ");
            double heuresTravail = Double.parseDouble(scanner.nextLine());
            System.out.print("Entrez le facteur de productivité : ");
            double productiviteOuvrier = Double.parseDouble(scanner.nextLine());

            MainOeuvre mainOeuvre = new MainOeuvre(0, nomMainOeuvre, "Main d'œuvre", 0.0, projet.getId(), tauxHoraire, heuresTravail, productiviteOuvrier);
            projet.ajouterComposant(mainOeuvre);
            composantService.ajouterComposant(mainOeuvre);
            System.out.println("Main-d'œuvre ajoutée avec succès !");

            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            if (scanner.nextLine().equalsIgnoreCase("n")) {
                break;
            }
        }

    }

    private static void appliquerTVA(Projet projet) {
        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            double pourcentageTVA = Double.parseDouble(scanner.nextLine());

            ComposantRepositoryImpl composantRepo = new ComposantRepositoryImpl();
            composantRepo.mettreAJourTauxTVA(projet.getId(), pourcentageTVA);
        } else {
            System.out.println("Aucune TVA appliquée.");
        }
    }

    private static void appliquerMargeBeneficiaire(Projet projet) {
        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            double nouvelleMarge = Double.parseDouble(scanner.nextLine());

            projet.setMargeBeneficiaire(nouvelleMarge);
            ProjetRepositoryImpl projetRepo = new ProjetRepositoryImpl();
            projetRepo.mettreAJourMargeBeneficiaire(projet.getId(), nouvelleMarge);
        } else {
            System.out.println("Aucune marge bénéficiaire appliquée.");
        }
    }
}
