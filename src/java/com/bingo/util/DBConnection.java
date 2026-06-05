package com.bingo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = System.getenv("DB_URL");

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Không tìm thấy PostgreSQL JDBC Driver.", ex);
        }

        String jdbcUrl = URL.replace("postgresql://", "jdbc:postgresql://");

        return DriverManager.getConnection(jdbcUrl);
    }
}