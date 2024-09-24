package com.baticuisine.repositories.impl;

import com.baticuisine.config.DatabaseConnection;
import com.baticuisine.models.Composant;
import com.baticuisine.models.MainOeuvre;
import com.baticuisine.models.Materiel;
import com.baticuisine.repositories.interfaces.ComposantRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComposantRepositoryImpl implements ComposantRepository {
    private final Connection connection;

    public ComposantRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterComposant(Composant composant) {
        String sql = "INSERT INTO Composant (nom, type, tauxTVA, projet_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, composant.getNom());
            statement.setString(2, composant.getType());

            // Handle null for tauxTVA
            if (composant.getTauxTVA() != null) {
                statement.setDouble(3, composant.getTauxTVA());
            } else {
                statement.setNull(3, java.sql.Types.DOUBLE); // Set SQL NULL if tauxTVA is null
            }

            statement.setInt(4, composant.getProjetId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    composant.setId(generatedId);

                    // Add logic for Materiel and MainOeuvre if needed
                    if (composant instanceof Materiel materiel) {
                        MaterielRepositoryImpl materielRepo = new MaterielRepositoryImpl();
                        materiel.setId(generatedId);
                        materielRepo.ajouterMateriel(materiel);
                    } else if (composant instanceof MainOeuvre mainOeuvre) {
                        MainOeuvreRepositoryImpl mainOeuvreRepo = new MainOeuvreRepositoryImpl();
                        mainOeuvre.setId(generatedId);
                        mainOeuvreRepo.ajouterMainOeuvre(mainOeuvre);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du composant : " + e.getMessage());
        }
    }

    @Override
    public void mettreAJourTauxTVA(int projetId, double nouveauTauxTVA) {
        String sql = "UPDATE Composant SET tauxTVA = ? WHERE projet_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, nouveauTauxTVA);
            statement.setInt(2, projetId);

            int rowsUpdated = statement.executeUpdate();
            System.out.println(rowsUpdated + " composants mis à jour avec succès avec le taux de TVA : " + nouveauTauxTVA + "%.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour des taux de TVA : " + e.getMessage());
        }
    }
}
