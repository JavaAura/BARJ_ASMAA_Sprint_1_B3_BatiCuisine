package com.baticuisine.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
            LOGGER.info("Database connection established successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to the database", e);
            throw new RuntimeException("Error connecting to the database", e);
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
                System.out.println("Connection to the database is successful.");
            } else {
                System.out.println("Connection to the database is closed or failed.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking the database connection", e);
        }
    }
}
