package model.dao.impl;

import java.sql.Connection;

public class BaseDao {
    protected static Connection connection;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static Connection getConnection() {
        return connection;
    }
}

