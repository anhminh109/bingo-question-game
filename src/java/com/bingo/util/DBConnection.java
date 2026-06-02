package com.bingo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = System.getenv("postgresql://anhminh:NRqMMpH6scPxiw1S9aWGrXkhdXLR9fXw@dpg-d8fh9brbc2fs73eoes3g-a.singapore-postgres.render.com/bingo_game_2ocr");

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Không tìm thấy PostgreSQL JDBC Driver.", ex);
        }

        return DriverManager.getConnection(URL);
    }
}