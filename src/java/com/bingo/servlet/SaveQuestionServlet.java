package com.bingo.servlet;

import com.bingo.dao.QuestionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "SaveQuestionServlet", urlPatterns = {"/save-questions"})
public class SaveQuestionServlet extends HttpServlet {
    private final QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        Map<Integer, String> questionTexts = new LinkedHashMap<>();

        for (int i = 1; i <= 25; i++) {
            questionTexts.put(i, request.getParameter("question_" + i));
        }

        try {
            questionDAO.saveAllQuestions(questionTexts);
            response.sendRedirect(request.getContextPath() + "/setting.jsp?saved=1");
        } catch (SQLException ex) {
            throw new ServletException("Không thể lưu câu hỏi.", ex);
        }
    }
}
