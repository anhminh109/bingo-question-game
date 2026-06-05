package com.bingo.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String DB_URL_ENV = "DB_URL";
    private static final String POSTGRESQL_URL_PREFIX = "postgresql://";
    private static final String JDBC_POSTGRESQL_URL_PREFIX = "jdbc:postgresql://";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv(DB_URL_ENV);
        boolean dbUrlExists = dbUrl != null && !dbUrl.trim().isEmpty();
        System.out.println("DB_URL environment variable exists: " + dbUrlExists);

        if (!dbUrlExists) {
            throw new SQLException("DB_URL environment variable is missing");
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", ex);
        }

        ConnectionConfig connectionConfig = toConnectionConfig(dbUrl.trim());

        return DriverManager.getConnection(connectionConfig.jdbcUrl, connectionConfig.properties);
    }

    private static ConnectionConfig toConnectionConfig(String dbUrl) throws SQLException {
        if (dbUrl.startsWith(JDBC_POSTGRESQL_URL_PREFIX)) {
            return new ConnectionConfig(dbUrl, new Properties());
        }

        if (dbUrl.startsWith(POSTGRESQL_URL_PREFIX)) {
            return parsePostgresqlUrl(dbUrl);
        }

        return new ConnectionConfig(dbUrl, new Properties());
    }

    private static ConnectionConfig parsePostgresqlUrl(String dbUrl) throws SQLException {
        URI uri;
        try {
            uri = new URI(dbUrl);
        } catch (URISyntaxException ex) {
            throw new SQLException("DB_URL environment variable is invalid", ex);
        }

        String host = uri.getHost();
        if (host == null || host.trim().isEmpty()) {
            throw new SQLException("DB_URL environment variable is invalid");
        }

        StringBuilder jdbcUrl = new StringBuilder(JDBC_POSTGRESQL_URL_PREFIX).append(host);
        if (uri.getPort() >= 0) {
            jdbcUrl.append(':').append(uri.getPort());
        }
        if (uri.getRawPath() != null) {
            jdbcUrl.append(uri.getRawPath());
        }
        if (uri.getRawQuery() != null && !uri.getRawQuery().isEmpty()) {
            jdbcUrl.append('?').append(uri.getRawQuery());
        }

        Properties properties = new Properties();
        String userInfo = uri.getUserInfo();
        if (userInfo != null && !userInfo.isEmpty()) {
            int separatorIndex = userInfo.indexOf(':');
            if (separatorIndex >= 0) {
                properties.setProperty("user", userInfo.substring(0, separatorIndex));
                properties.setProperty("password", userInfo.substring(separatorIndex + 1));
            } else {
                properties.setProperty("user", userInfo);
            }
        }

        return new ConnectionConfig(jdbcUrl.toString(), properties);
    }

    private static final class ConnectionConfig {
        private final String jdbcUrl;
        private final Properties properties;

        private ConnectionConfig(String jdbcUrl, Properties properties) {
            this.jdbcUrl = jdbcUrl;
            this.properties = properties;
        }
    }
}
