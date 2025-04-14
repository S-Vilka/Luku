/**
 * This package contains the implementation of Data Access Objects (DAOs)
 * for managing entities in the system. It includes classes for handling
 * database operations related to notifications, reservations, and users.
 */
package model.dao.impl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static config.DatabaseConfig.DB_URL;
import static config.DatabaseConfig.USER;
import static config.DatabaseConfig.PASSWORD;

public final class BaseDao {
    /**
     * The database connection URL.
     */
    private static Connection connection;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(
                    "Failed to load MariaDB JDBC driver "
                            + "or establish connection", e
            );
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private BaseDao() {
        // Prevent instantiation
    }

    /**
     * Returns the current database connection.
     * @param conn the connection to be set
     *
     */
    public static void setConnection(final Connection conn) {
        connection = conn;
    }

    /**
     * Returns the current database connection.
     * @return the database connection
     */
    public static Connection getConnection() {
        return connection;
    }

}

