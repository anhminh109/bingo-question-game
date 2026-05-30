package com.bingo.servlet;

import com.bingo.dao.QuestionDAO;
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

        try {
            questionDAO.resetQuestions();
            response.sendRedirect(request.getContextPath() + "/setting.jsp?reset=1");
        } catch (SQLException ex) {
            throw new ServletException("Không thể reset câu hỏi.", ex);
        }
    }
}
