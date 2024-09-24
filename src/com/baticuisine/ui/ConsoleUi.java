package com.baticuisine.ui;

import com.baticuisine.models.Client;
import com.baticuisine.models.Projet;
import com.baticuisine.models.Devis;
import com.baticuisine.models.Composant;
import com.baticuisine.models.Projet.EtatProjet;
import com.baticuisine.models.Materiel;
import com.baticuisine.models.MainOeuvre;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjetService;
import com.baticuisine.services.ComposantService;
import com.baticuisine.repositories.impl.ComposantRepositoryImpl;
import com.baticuisine.repositories.impl.ProjetRepositoryImpl;
import com.baticuisine.repositories.impl.DevisRepositoryImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUi {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProjetRepositoryImpl projetRepo = new ProjetRepositoryImpl();
    private static final ProjetService projetService = new ProjetService();

    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ComposantService composantService = new ComposantService();
        projetService.chargerProjets();

        while (true) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");
            int option = validateIntegerInput();

            switch (option) {
                case 1:
                    Client client = choisirClient(clientService);
                    if (client != null) {
                        Projet projet = ajouterProjet(projetService, client);
                        ajouterComposants(projet, composantService);
                        projetService.mettreAJourCoutTotal(projet);
                        appliquerTVA(projet);
                        appliquerMargeBeneficiaire(projet);
                        enregistrerDevis(projet);
                    } else {
                        System.out.println("Impossible d'ajouter un projet sans client.");
                    }
                    break;
                case 2:
                    afficherProjets();
                    break;
                case 3:
                    afficherDetailsProjet();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void afficherDetailsProjet() {
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();

        Optional<Projet> projetOpt = projetService.chercherProjetParNom(nomProjet);

        projetOpt.ifPresentOrElse(
                projet -> {
                    System.out.println("Nom du projet : " + projet.getNomProjet());
                    System.out.println("Client : " + projet.getClient().getNom());
                    System.out.println("Adresse du chantier : " + projet.getClient().getAdresse());
                    System.out.println("Surface : " + projet.getSurface() + " m²");
                    projet.afficherResultats();
                },
                () -> System.out.println("Projet non trouvé.")
        );
    }

    private static Client choisirClient(ClientService clientService) {
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int option = validateIntegerInput();

        return (option == 1) ? chercherClientExistant(clientService) : ajouterNouveauClient(clientService);
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
            if (scanner.nextLine().equalsIgnoreCase("y")) {
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
        boolean estProfessionnel = validateBooleanInput();

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
        double surface = validateDoubleInput();

        Projet projet = new Projet(nomProjet, surface, null, EtatProjet.ENCOURS, client);
        projetService.ajouterProjet(projet);
        return projet;
    }

    private static void ajouterComposants(Projet projet, ComposantService composantService) {
        while (true) {
            System.out.println("--- Ajout des matériaux ---");
            System.out.print("Entrez le nom du matériau : ");
            String nomMateriel = scanner.nextLine();
            System.out.print("Entrez la quantité de ce matériau : ");
            double quantite = validateDoubleInput();
            System.out.print("Entrez le coût unitaire de ce matériau : ");
            double coutUnitaire = validateDoubleInput();
            System.out.print("Entrez le coût de transport de ce matériau : ");
            double coutTransport = validateDoubleInput();
            System.out.print("Entrez le coefficient de qualité du matériau : ");
            double coefficientQualite = validateDoubleInput();

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
            double tauxHoraire = validateDoubleInput();
            System.out.print("Entrez le nombre d'heures travaillées : ");
            double heuresTravail = validateDoubleInput();
            System.out.print("Entrez le facteur de productivité : ");
            double productiviteOuvrier = validateDoubleInput();

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
            double pourcentageTVA = validateDoubleInput();

            ComposantRepositoryImpl composantRepo = new ComposantRepositoryImpl();
            composantRepo.mettreAJourTauxTVA(projet.getId(), pourcentageTVA);
            projet.calculerCoutTotal();
        } else {
            System.out.println("Aucune TVA appliquée.");
        }
    }

    private static void appliquerMargeBeneficiaire(Projet projet) {
        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            double nouvelleMarge = validateDoubleInput();

            projet.setMargeBeneficiaire(nouvelleMarge);
            projetRepo.mettreAJourMargeBeneficiaire(projet.getId(), nouvelleMarge);
        } else {
            System.out.println("Aucune marge bénéficiaire appliquée.");
        }
        projet.afficherResultats();
    }

    private static void enregistrerDevis(Projet projet) {
        System.out.println("--- Enregistrement du Devis ---");
        System.out.print("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
        String dateEmissionStr = scanner.nextLine();
        System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
        String dateValiditeStr = scanner.nextLine();

        LocalDate dateEmission = LocalDate.parse(dateEmissionStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateValidite = LocalDate.parse(dateValiditeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Souhaitez-vous enregistrer le devis ? (y/n) : ");
        String choix = scanner.nextLine();

        Devis devis = new Devis(0, projet.getCoutTotal(), dateEmission, dateValidite, choix.equalsIgnoreCase("y"), projet.getId());

        DevisRepositoryImpl devisRepo = new DevisRepositoryImpl();

        if (choix.equalsIgnoreCase("y")) {
            devisRepo.enregistrerDevis(devis);
            projet.setEtatProjet(Projet.EtatProjet.TERMINE);
            projetRepo.mettreAJourEtatProjet(projet);
            System.out.println("Devis enregistré avec succès et projet marqué comme terminé.");
        } else {
            projet.setEtatProjet(Projet.EtatProjet.ANNULE);
            projetRepo.mettreAJourEtatProjet(projet);
            System.out.println("Devis non enregistré, projet annulé.");
        }
    }

    private static void afficherProjets() {
        try {
            List<Projet> projets = projetRepo.recupererTousLesProjets();
            int dernierProjetId = -1;

            for (Projet projet : projets) {
                int projetId = projet.getId();

                if (projetId != dernierProjetId) {
                    if (dernierProjetId != -1) {
                        System.out.println("-------------------------------");
                    }
                    System.out.println("Nom du Projet: " + projet.getNomProjet());
                    System.out.println("Surface: " + projet.getSurface() + " m²");
                    System.out.println("Marge Bénéficiaire: " + projet.getMargeBeneficiaire() + "%");
                    System.out.println("Coût Total: " + projet.getCoutTotal() + " €");
                    System.out.println("État du Projet: " + projet.getEtatProjet().name());
                    System.out.println("Nom du Client: " + projet.getClient().getNom());
                    System.out.println("Adresse: " + projet.getClient().getAdresse());
                    System.out.println("Téléphone: " + projet.getClient().getTelephone());
                    System.out.println();
                    System.out.println("__Ses composants__");
                    dernierProjetId = projetId;
                }

                for (Composant composant : projet.getComposants()) {
                    System.out.println("Nom du Composant: " + composant.getNom());
                    System.out.println("Type du Composant: " + composant.getType());
                }
            }

            if (dernierProjetId != -1) {
                System.out.println("-------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage des projets : " + e.getMessage());
        }
    }

    // Input validation methods
    private static int validateIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre entier : ");
            }
        }
    }

    private static double validateDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre valide : ");
            }
        }
    }

    private static boolean validateBooleanInput() {
        while (true) {
            String input = scanner.nextLine();
            if ("true".equalsIgnoreCase(input)) {
                return true;
            } else if ("false".equalsIgnoreCase(input)) {
                return false;
            } else {
                System.out.print("Entrée invalide. Veuillez entrer 'true' ou 'false' : ");
            }
        }
    }
}
