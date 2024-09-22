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

            if (composant.getTauxTVA() != null) {
                statement.setDouble(3, composant.getTauxTVA());
            } else {
                statement.setNull(3, java.sql.Types.DOUBLE);
            }

            statement.setInt(4, composant.getProjetId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    composant.setId(generatedId);

                    if (composant instanceof Materiel) {
                        MaterielRepositoryImpl materielRepo = new MaterielRepositoryImpl();
                        ((Materiel) composant).setId(generatedId);
                        materielRepo.ajouterMateriel((Materiel) composant);
                    } else if (composant instanceof MainOeuvre) {
                        MainOeuvreRepositoryImpl mainOeuvreRepo = new MainOeuvreRepositoryImpl();
                        ((MainOeuvre) composant).setId(generatedId);
                        mainOeuvreRepo.ajouterMainOeuvre((MainOeuvre) composant);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du composant : " + e.getMessage());
        }
    }
}
