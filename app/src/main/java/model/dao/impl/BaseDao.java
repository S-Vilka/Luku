package model.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDao {
    protected static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/library_db", "library_user", "library_password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static Connection getConnection() {
        return connection;
    }
}

