package model.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static config.DatabaseConfig.*;

public class BaseDao {
    protected static Connection connection;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to load MariaDB JDBC driver or establish connection", e);
        }
    }


    public static void setConnection(Connection conn) {
        connection = conn;
    }

}

