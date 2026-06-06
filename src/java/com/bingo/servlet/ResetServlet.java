package com.bingo.servlet;

import com.bingo.dao.QuestionDAO;
import com.bingo.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ResetServlet", urlPatterns = {"/reset-questions"})
public class ResetServlet extends HttpServlet {
    private final QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            questionDAO.resetQuestions(userId);
            response.sendRedirect(request.getContextPath() + "/settings.jsp?reset=1");
        } catch (SQLException ex) {
            throw new ServletException("Không thể reset câu hỏi.", ex);
        }
    }
}
