package com.baticuisine.ui;

import com.baticuisine.models.*;
import com.baticuisine.models.Projet.EtatProjet;
import com.baticuisine.services.*;
import com.baticuisine.repositories.impl.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.Scanner;
import java.sql.SQLException;
public class ConsoleUi {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProjetRepositoryImpl projetRepo = new ProjetRepositoryImpl();
    private static final ProjetService projetService = new ProjetService();

    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ComposantService composantService = new ComposantService();

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
                    System.out.println("Coût total : " + projet.getCoutTotal() + " €");
                    System.out.println("État du projet : " + projet.getEtatProjet());

                    // Affichage des devis
                    if (projet.getDevis() != null) {
                        Devis devis = projet.getDevis();
                        System.out.println("Montant estimé : " + devis.getMontantEstime() + " €");
                        System.out.println("Date d'émission : " + devis.getDateEmission());
                        System.out.println("Date de validité : " + devis.getDateValidite());
                        System.out.println("Accepté : " + (devis.isAccepte() ? "Oui" : "Non"));
                    }

                    // Affichage des composants
                    System.out.println("Composants : ");
                    for (Composant composant : projet.getComposants()) {
                        System.out.println(" - Nom : " + composant.getNom() + ", Type : " + composant.getType());
                    }
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

    public static void appliquerTVA(Projet projet) {
        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            double pourcentageTVA = validateDoubleInput();

            // Appel du repository pour mettre à jour la TVA
            ComposantRepositoryImpl composantRepo = new ComposantRepositoryImpl();
            composantRepo.mettreAJourTauxTVA(projet.getId(), pourcentageTVA);

            // Calcul du coût total après application de la TVA
            calculerCoutTotal(projet);
        } else {
            System.out.println("Aucune TVA appliquée.");
        }
    }

    public static void appliquerMargeBeneficiaire(Projet projet) {
        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            double nouvelleMarge = validateDoubleInput();

            // Mise à jour de la marge bénéficiaire
            projet.setMargeBeneficiaire(nouvelleMarge);
            projetRepo.mettreAJourMargeBeneficiaire(projet.getId(), nouvelleMarge);
        } else {
            System.out.println("Aucune marge bénéficiaire appliquée.");
        }

        // Affichage des résultats après application de la marge bénéficiaire
        afficherResultats(projet);
    }

    public static void calculerCoutTotal(Projet projet) {
        // Calcul du coût des matériaux
        double totalMateriaux = projet.getComposants().stream()
                .filter(composant -> composant instanceof Materiel)
                .mapToDouble(composant -> {
                    Materiel materiel = (Materiel) composant;
                    return (materiel.getCoutUnitaire() * materiel.getQuantite()) + materiel.getCoutTransport();
                })
                .sum();

        double totalMainOeuvre = projet.getComposants().stream()
                .filter(composant -> composant instanceof MainOeuvre)
                .mapToDouble(composant -> {
                    MainOeuvre mainOeuvre = (MainOeuvre) composant;
                    return mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                })
                .sum();

        double totalMateriauxAvecTVA = totalMateriaux * 1.2;
        double totalMainOeuvreAvecTVA = totalMainOeuvre * 1.2;

        double totalAvantMarge = totalMateriauxAvecTVA + totalMainOeuvreAvecTVA;

        projet.setCoutTotal(totalAvantMarge);
    }

    public static void afficherResultats(Projet projet) {
        double totalMateriaux = projet.getComposants().stream()
                .filter(composant -> composant instanceof Materiel)
                .mapToDouble(composant -> {
                    Materiel materiel = (Materiel) composant;
                    return (materiel.getCoutUnitaire() * materiel.getQuantite()) + materiel.getCoutTransport();
                })
                .sum();

        double totalMainOeuvre = projet.getComposants().stream()
                .filter(composant -> composant instanceof MainOeuvre)
                .mapToDouble(composant -> {
                    MainOeuvre mainOeuvre = (MainOeuvre) composant;
                    return mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                })
                .sum();

        double totalMateriauxAvecTVA = totalMateriaux * 1.2;
        double totalMainOeuvreAvecTVA = totalMainOeuvre * 1.2;
        double totalAvantMarge = totalMateriauxAvecTVA + totalMainOeuvreAvecTVA;

        double margeBeneficiaire = totalAvantMarge * (projet.getMargeBeneficiaire() / 100);
        double coutTotalFinal = totalAvantMarge + margeBeneficiaire;

        // Affichage des résultats
        System.out.println("--- Résultat du Calcul ---");
        System.out.println("Coût total des matériaux avant TVA : " + totalMateriaux + " €");
        System.out.println("Coût total des matériaux avec TVA : " + totalMateriauxAvecTVA + " €");
        System.out.println("Coût total de la main-d'œuvre avant TVA : " + totalMainOeuvre + " €");
        System.out.println("Coût total de la main-d'œuvre avec TVA : " + totalMainOeuvreAvecTVA + " €");
        System.out.println("Coût total avant marge : " + totalAvantMarge + " €");
        System.out.println("Marge bénéficiaire (" + projet.getMargeBeneficiaire() + "%) : " + margeBeneficiaire + " €");
        System.out.println("Coût total final du projet : " + coutTotalFinal + " €");
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
        try (ResultSet rs = projetRepo.recupererTousLesProjets()) {
            int dernierProjetId = -1;  // Pour suivre le projet actuel
            while (rs != null && rs.next()) {
                int projetId = rs.getInt("projet_id");

                if (projetId != dernierProjetId) {
                    if (dernierProjetId != -1) {
                        // Séparation entre deux projets
                        System.out.println("-------------------------------");
                    }
                    System.out.println("Nom du Projet: " + rs.getString("nom_projet"));
                    System.out.println("Surface: " + rs.getDouble("surface") + " m²");
                    System.out.println("Marge Bénéficiaire: " + rs.getDouble("margeBeneficiaire") + "%");
                    System.out.println("Coût Total: " + rs.getDouble("coutTotal") + " €");
                    System.out.println("État du Projet: " + rs.getString("etatProjet"));
                    System.out.println("Nom du Client: " + rs.getString("client_nom"));
                    System.out.println("Adresse: " + rs.getString("client_adresse"));
                    System.out.println("Téléphone: " + rs.getString("client_telephone"));
                    System.out.println();
                    System.out.println("__Ses composants__");
                    dernierProjetId = projetId;
                }

                System.out.println("Nom du Composant: " + rs.getString("composant_nom"));
                System.out.println("Type du Composant: " + rs.getString("composant_type"));

                System.out.println();
            }

            if (dernierProjetId != -1) {
                System.out.println("-------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des projets : " + e.getMessage());
        }
    }



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
