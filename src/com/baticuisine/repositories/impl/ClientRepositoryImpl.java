package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Client;
import com.baticuisine.repositories.interfaces.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRepositoryImpl implements ClientRepository {
    private final Connection connection;

    public ClientRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterClient(Client client) {
        if (clientExists(client.getNom())) {
            System.out.println("ce client existe déjà.");
            return;
        }

        String sql = "INSERT INTO Client (nom, adresse, telephone, estProfessionnel) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getNom());
            statement.setString(2, client.getAdresse());
            statement.setString(3, client.getTelephone());
            statement.setBoolean(4, client.isEstProfessionnel());

            statement.executeUpdate();
            System.out.println("Client ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }

    private boolean clientExists(String nom) {
        String sql = "SELECT COUNT(*) FROM Client WHERE nom = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nom);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du client : " + e.getMessage());
        }

        return false;
    }
}
