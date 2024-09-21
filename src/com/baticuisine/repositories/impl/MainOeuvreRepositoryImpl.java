package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.MainOeuvre;
import com.baticuisine.repositories.interfaces.MainOeuvreRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainOeuvreRepositoryImpl implements MainOeuvreRepository {
    private final Connection connection;

    public MainOeuvreRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterMainOeuvre(MainOeuvre mainOeuvre) {
        String sql = "INSERT INTO MainOeuvre (composant_id, tauxHoraire, heuresTravail, productiviteOuvrier) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, mainOeuvre.getId());
            statement.setDouble(2, mainOeuvre.getTauxHoraire());
            statement.setDouble(3, mainOeuvre.getHeuresTravail());
            statement.setDouble(4, mainOeuvre.getProductiviteOuvrier());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la main-d'œuvre : " + e.getMessage());
        }
    }
}
