package com.bingo.util;

public class PasswordService {

    public String hash(String rawPassword) {
        return rawPassword == null ? "" : rawPassword;
    }

    public boolean matches(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }

        return hash(rawPassword).equals(storedPassword);
    }
}
