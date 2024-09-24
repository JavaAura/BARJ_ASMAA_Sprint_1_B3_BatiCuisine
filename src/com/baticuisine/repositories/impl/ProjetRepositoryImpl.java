package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Projet;
import com.baticuisine.repositories.interfaces.ProjetRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Projet> recupererTousLesProjets() {
        List<Projet> projets = new ArrayList<>();
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
                "c.telephone AS client_telephone " +
                "FROM Projet p " +
                "LEFT JOIN Client c ON p.client_id = c.id " +
                "ORDER BY p.id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Projet projet = new Projet(
                        resultSet.getString("nom_projet"),
                        resultSet.getDouble("surface"),
                        resultSet.getDouble("margeBeneficiaire"),
                        Projet.EtatProjet.valueOf(resultSet.getString("etatProjet")),
                        null
                );
                projet.setId(resultSet.getInt("projet_id"));
                projet.setCoutTotal(resultSet.getDouble("coutTotal"));
                projets.add(projet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des projets : " + e.getMessage());
        }
        return projets;
    }
}
