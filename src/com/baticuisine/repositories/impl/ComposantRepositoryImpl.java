package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Composant;
import com.baticuisine.models.MainOeuvre;
import com.baticuisine.models.Materiel;
import com.baticuisine.repositories.interfaces.ComposantRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Import nécessaire
import java.sql.SQLException;

public class ComposantRepositoryImpl implements ComposantRepository {
    private final Connection connection;

    public ComposantRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterComposant(Composant composant) {
        String sql = "INSERT INTO Composant (nom, type, tauxTVA) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, composant.getNom());
            statement.setString(2, composant.getType());
            statement.setDouble(3, composant.getTauxTVA());

            // Execute the insertion and get the generated ID
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    composant.setId(generatedId); // Set the generated ID to the composant

                    if (composant instanceof Materiel) {
                        ajouterMateriel((Materiel) composant);
                    } else if (composant instanceof MainOeuvre) {
                        ajouterMainOeuvre((MainOeuvre) composant);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du composant : " + e.getMessage());
        }
    }

    private void ajouterMateriel(Materiel materiel) {
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

    private void ajouterMainOeuvre(MainOeuvre mainOeuvre) {
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
