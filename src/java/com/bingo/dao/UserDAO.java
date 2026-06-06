package com.bingo.dao;

import com.bingo.model.User;
import com.bingo.util.DBConnection;
import com.bingo.util.PasswordService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    private final PasswordService passwordService = new PasswordService();

    public void ensureSchema() throws SQLException {
        String createSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id SERIAL PRIMARY KEY, "
                + "username VARCHAR(100) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL"
                + ")";

        String seedSql = "INSERT INTO users (username, password) "
                + "VALUES (?, ?) "
                + "ON CONFLICT (username) DO NOTHING";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement createPs = conn.prepareStatement(createSql);
             PreparedStatement seedPs = conn.prepareStatement(seedSql)) {

            createPs.executeUpdate();

            seedPs.setString(1, DEFAULT_USERNAME);
            seedPs.setString(2, passwordService.hash(DEFAULT_PASSWORD));
            seedPs.executeUpdate();
        }
    }

    public User authenticate(String username, String password) throws SQLException {
        User user = findByUsername(username);
        if (user == null || !passwordService.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    public User findByUsername(String username) throws SQLException {
        ensureSchema();

        String cleanedUsername = cleanUsername(username);
        if (cleanedUsername.isEmpty()) {
            return null;
        }

        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cleanedUsername);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        }

        return null;
    }

    public int getAdminUserId() throws SQLException {
        User admin = findByUsername(DEFAULT_USERNAME);
        if (admin == null) {
            throw new SQLException("Default admin user could not be created.");
        }

        return admin.getId();
    }

    private String cleanUsername(String username) {
        return username == null ? "" : username.trim();
    }
}
