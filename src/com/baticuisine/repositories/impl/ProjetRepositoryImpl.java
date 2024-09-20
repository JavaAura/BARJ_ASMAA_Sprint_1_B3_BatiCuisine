package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Projet;
import com.baticuisine.repositories.interfaces.ProjetRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjetRepositoryImpl implements ProjetRepository {
    private final Connection connection;

    public ProjetRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterProjet(Projet projet) {
        String sql = "INSERT INTO Projet (nomProjet, surface, margeBeneficiaire, coutTotal, etatProjet, client_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, projet.getNomProjet());
            statement.setDouble(2, projet.getSurface());
            statement.setDouble(3, projet.getMargeBeneficiaire());
            statement.setDouble(4, projet.getCoutTotal());
            statement.setString(5, projet.getEtatProjet().name());
            statement.setInt(6, projet.getClient().getId());

            statement.executeUpdate();
            System.out.println("Projet ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du projet : " + e.getMessage());
        }
    }

}
