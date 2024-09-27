package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Client;
import com.baticuisine.models.Projet;
import com.baticuisine.repositories.interfaces.ProjetRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProjetRepositoryImpl implements ProjetRepository {
    private final Connection connection;

    public ProjetRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterProjet(Projet projet) {
        String sql = "INSERT INTO Projet (nomProjet, surface, margeBeneficiaire, coutTotal, etatProjet, client_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, projet.getNomProjet());
            statement.setDouble(2, projet.getSurface());
            statement.setObject(3, projet.getMargeBeneficiaire(), java.sql.Types.DOUBLE);
            statement.setDouble(4, projet.getCoutTotal());
            statement.setString(5, projet.getEtatProjet().name());
            statement.setInt(6, projet.getClient().getId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    projet.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du projet : " + e.getMessage());
        }
    }

    @Override
    public void mettreAJourCoutTotal(Projet projet) {
        String sql = "UPDATE Projet SET coutTotal = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, projet.getCoutTotal());
            statement.setInt(2, projet.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du coût total du projet : " + e.getMessage());
        }
    }

    @Override
    public void mettreAJourMargeBeneficiaire(int projetId, double nouvelleMarge) {
        String sql = "UPDATE Projet SET margeBeneficiaire = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, nouvelleMarge);
            statement.setInt(2, projetId);
            statement.executeUpdate();
            System.out.println("Marge bénéficiaire mise à jour avec succès : " + nouvelleMarge + "%.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la marge bénéficiaire : " + e.getMessage());
        }
    }

    @Override
    public void mettreAJourEtatProjet(Projet projet) {
        String sql = "UPDATE Projet SET etatProjet = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, projet.getEtatProjet().name());
            statement.setInt(2, projet.getId());
            statement.executeUpdate();
            System.out.println("État du projet mis à jour avec succès : " + projet.getEtatProjet());
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'état du projet : " + e.getMessage());
        }
    }

    public ResultSet recupererTousLesProjets() {
        String sql = "SELECT " +
                "p.id AS projet_id, " +
                "p.nomProjet AS nom_projet, " +
                "p.surface, " +
                "p.margeBeneficiaire, " +
                "p.coutTotal, " +
                "p.etatProjet, " +
                "c.id AS client_id, " +
                "c.nom AS client_nom, " +
                "c.adresse AS client_adresse, " +
                "c.telephone AS client_telephone, " +
                "comp.id AS composant_id, " +
                "comp.nom AS composant_nom, " +
                "comp.type AS composant_type, " +
                "mo.tauxHoraire AS taux_horaire_main_oeuvre, " +
                "mo.heuresTravail AS heures_travail, " +
                "mo.productiviteOuvrier AS productivite_ouvrier, " +
                "mat.quantite AS quantite_materiel, " +
                "mat.coutUnitaire AS cout_unitaire_materiel, " +
                "mat.coutTransport AS cout_transport_materiel, " +
                "mat.coefficientQualite AS coefficient_qualite_materiel " +
                "FROM Projet p " +
                "LEFT JOIN Client c ON p.client_id = c.id " +
                "LEFT JOIN Composant comp ON comp.projet_id = p.id " +
                "LEFT JOIN MainOeuvre mo ON mo.composant_id = comp.id " +
                "LEFT JOIN Materiel mat ON mat.composant_id = comp.id " +
                "ORDER BY p.id";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des projets : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Projet> findByIdWithDetails(int projetId) {
        String query = "SELECT p.*, c.nom AS client_nom, c.adresse AS client_adresse, c.telephone AS client_telephone " +
                "FROM Projet p " +
                "JOIN Client c ON p.client_id = c.id " +
                "WHERE p.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, projetId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Projet projet = new Projet(
                        resultSet.getString("nomProjet"),
                        resultSet.getDouble("surface"),
                        resultSet.getDouble("margeBeneficiaire"),
                        Projet.EtatProjet.valueOf(resultSet.getString("etatProjet")),
                        new Client(
                                resultSet.getString("client_nom"),
                                resultSet.getString("client_adresse"),
                                resultSet.getString("client_telephone"),
                                false
                        )
                );
                projet.setId(resultSet.getInt("id")); // Set the project ID
                return Optional.of(projet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du projet avec les détails : " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Projet> findByName(String nomProjet) {
        String sql = "SELECT p.*, c.nom AS client_nom, c.adresse AS client_adresse, c.telephone AS client_telephone " +
                "FROM Projet p " +
                "LEFT JOIN Client c ON p.client_id = c.id " +
                "WHERE p.nomProjet = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nomProjet);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Projet projet = new Projet(
                        resultSet.getString("nomProjet"),
                        resultSet.getDouble("surface"),
                        resultSet.getDouble("margeBeneficiaire"),
                        Projet.EtatProjet.valueOf(resultSet.getString("etatProjet")),
                        new Client(
                                resultSet.getString("client_nom"),
                                resultSet.getString("client_adresse"),
                                resultSet.getString("client_telephone"),
                                false
                        )
                );
                projet.setId(resultSet.getInt("id"));
                return Optional.of(projet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du projet par nom : " + e.getMessage());
        }
        return Optional.empty();


   }
}
