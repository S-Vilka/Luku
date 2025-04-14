package config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String DB_URL = dotenv.get("DB_URL");
    public static final String USER = dotenv.get("DB_USER");
    public static final String PASSWORD = dotenv.get("DB_PASSWORD");

    static {
        // Print environment variables to verify they are being read correctly.
        System.out.println("DB_URL: " + DB_URL);
        System.out.println("DB_USER: " + USER);
        System.out.println("DB_PASSWORD: " + PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        if (DB_URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Database connection details are not set in environment variables.");
        }
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
