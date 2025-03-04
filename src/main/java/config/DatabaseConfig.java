package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mariadb://localhost:3306/library_db";
    public static final String USER = "library_user";
    public static final String PASSWORD = "library_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}