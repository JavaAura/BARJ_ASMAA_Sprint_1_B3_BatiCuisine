package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Projet;
import com.baticuisine.repositories.interfaces.ProjetRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

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
}
