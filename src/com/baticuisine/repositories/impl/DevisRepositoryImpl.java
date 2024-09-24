package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Devis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DevisRepositoryImpl {
    private final Connection connection;

    public DevisRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void enregistrerDevis(Devis devis) {
        String sql = "INSERT INTO Devis (montantEstime, dateEmission, dateValidite, accepte, projet_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, devis.getMontantEstime());
            statement.setDate(2, java.sql.Date.valueOf(devis.getDateEmission()));
            statement.setDate(3, java.sql.Date.valueOf(devis.getDateValidite()));
            statement.setBoolean(4, devis.isAccepte());
            statement.setInt(5, devis.getProjetId());
            statement.executeUpdate();
            System.out.println("Devis enregistré avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement du devis : " + e.getMessage());
        }
    }
}
