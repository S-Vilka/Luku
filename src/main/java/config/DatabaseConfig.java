package config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
    /**
     * Database connection details.
     * These should be set in the .env file in the root of the project.
     */
    private static final Dotenv DOTENV = Dotenv.load();
    /** URL of the database. */
    public static final String DB_URL = DOTENV.get("DB_URL");
    /** Database username. */
    public static final String USER = DOTENV.get("DB_USER");
    /**
     * Database password.
     */
    public static final String PASSWORD = DOTENV.get("DB_PASSWORD");

    private DatabaseConfig() {
        throw new UnsupportedOperationException("Utility class");
    }
    static {
        // Print environment variables to verify they are being read correctly
        System.out.println("DB_URL: " + DB_URL);
        System.out.println("DB_USER: " + USER);
        System.out.println("DB_PASSWORD: " + PASSWORD);
    }

    /**
     * Establishes a connection to the database
     * using the provided URL, username, and password.
     * @return A Connection object to interact with the database.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (DB_URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Database connection details are not set");
        }
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
