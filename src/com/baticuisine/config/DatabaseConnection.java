package com.baticuisine.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static DatabaseConnection instance;
    private final Connection connection;
    private static final String URL = "jdbc:postgresql://localhost:5432/baticuisine";
    private static final String USER = "postgres";
    private static final String PASSWORD = "asmaa123";

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.info("Connexion à la base de données établie avec succès.");
            createTables();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur de connexion à la base de données", e);
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void testConnection() {
        DatabaseConnection dbConnection = getInstance();
        Connection connection = dbConnection.getConnection();

        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connexion à la base de données réussie.");
            } else {
                System.out.println("La connexion à la base de données est fermée ou a échoué.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de la connexion", e);
        }
    }

    private void createTables() {
        try (Statement statement = connection.createStatement()) {

            String createClientTable = "CREATE TABLE IF NOT EXISTS Client (" +
                    "id SERIAL PRIMARY KEY," +
                    "nom VARCHAR(255) NOT NULL," +
                    "adresse VARCHAR(255)," +
                    "telephone VARCHAR(20)," +
                    "estProfessionnel BOOLEAN" +
                    ")";
            statement.executeUpdate(createClientTable);

            String createProjetTable = "CREATE TABLE IF NOT EXISTS Projet (" +
                    "id SERIAL PRIMARY KEY," +
                    "nomProjet VARCHAR(255) NOT NULL," +
                    "surface DOUBLE PRECISION," +
                    "margeBeneficiaire DOUBLE PRECISION DEFAULT NULL," +
                    "coutTotal DOUBLE PRECISION," +
                    "etatProjet VARCHAR(50) CHECK (etatProjet IN ('ENCOURS', 'TERMINE', 'ANNULE'))," +
                    "client_id INTEGER REFERENCES Client(id)" +
                    ")";
            statement.executeUpdate(createProjetTable);

            String createDevisTable = "CREATE TABLE IF NOT EXISTS Devis (" +
                    "id SERIAL PRIMARY KEY," +
                    "montantEstime DOUBLE PRECISION," +
                    "dateEmission DATE," +
                    "dateValidite DATE," +
                    "accepte BOOLEAN," +
                    "projet_id INTEGER REFERENCES Projet(id)" +
                    ")";
            statement.executeUpdate(createDevisTable);

            String createComposantTable = "CREATE TABLE IF NOT EXISTS Composant (" +
                    "id SERIAL PRIMARY KEY," +
                    "nom VARCHAR(255) NOT NULL," +
                    "type VARCHAR(50)," +
                    "tauxTVA DOUBLE PRECISION," +
                    "projet_id INTEGER REFERENCES Projet(id)" +
                    ")";
            statement.executeUpdate(createComposantTable);

            String createMainOeuvreTable = "CREATE TABLE IF NOT EXISTS MainOeuvre (" +
                    "composant_id INTEGER PRIMARY KEY REFERENCES Composant(id)," +
                    "tauxHoraire DOUBLE PRECISION," +
                    "heuresTravail DOUBLE PRECISION," +
                    "productiviteOuvrier DOUBLE PRECISION" +
                    ")";
            statement.executeUpdate(createMainOeuvreTable);

            String createMaterielTable = "CREATE TABLE IF NOT EXISTS Materiel (" +
                    "composant_id INTEGER PRIMARY KEY REFERENCES Composant(id)," +
                    "quantite DOUBLE PRECISION," +
                    "coutUnitaire DOUBLE PRECISION," +
                    "coutTransport DOUBLE PRECISION," +
                    "coefficientQualite DOUBLE PRECISION" +
                    ")";
            statement.executeUpdate(createMaterielTable);

            LOGGER.info("Tables créées avec succès .");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création des tables", e);
        }
    }

}
