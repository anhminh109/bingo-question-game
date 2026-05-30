package com.bingo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=bingo_game;encrypt=true;trustServerCertificate=true";;
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Không tìm thấy SQL Server JDBC Driver. Hãy thêm mssql-jdbc vào WEB-INF/lib.", ex);
        }

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
