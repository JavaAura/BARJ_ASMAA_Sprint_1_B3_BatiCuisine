package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Materiel;
import com.baticuisine.repositories.interfaces.MaterielRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MaterielRepositoryImpl implements MaterielRepository {
    private final Connection connection;

    public MaterielRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterMateriel(Materiel materiel) {
        String sql = "INSERT INTO Materiel (composant_id, quantite, coutUnitaire, coutTransport, coefficientQualite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, materiel.getId());
            statement.setDouble(2, materiel.getQuantite());
            statement.setDouble(3, materiel.getCoutUnitaire());
            statement.setDouble(4, materiel.getCoutTransport());
            statement.setDouble(5, materiel.getCoefficientQualite());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du matériel : " + e.getMessage());
        }
    }
}
