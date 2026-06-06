package com.bingo.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public final class SessionUtil {

    private SessionUtil() {
    }

    public static Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute("userId");
        if (userId instanceof Integer) {
            return (Integer) userId;
        }
        if (userId instanceof Number) {
            return ((Number) userId).intValue();
        }
        if (userId instanceof String) {
            try {
                return Integer.valueOf((String) userId);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "";
        }

        Object username = session.getAttribute("username");
        return username == null ? "" : username.toString();
    }

    public static boolean requireLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (getUserId(request) != null) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return false;
    }
}
