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
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "SaveQuestionServlet", urlPatterns = {"/save-questions"})
public class SaveQuestionServlet extends HttpServlet {
    private final QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        Map<Integer, String> questionTexts = new LinkedHashMap<>();
        Map<Integer, String> answers = new LinkedHashMap<>();

        for (int i = 1; i <= 25; i++) {
            questionTexts.put(i, request.getParameter("question_" + i));
            answers.put(i, request.getParameter("answer_" + i));
        }

        try {
            questionDAO.saveAllQuestions(userId, questionTexts, answers);
            response.sendRedirect(request.getContextPath() + "/settings.jsp?saved=1");
        } catch (SQLException ex) {
            throw new ServletException("Không thể lưu câu hỏi và đáp án.", ex);
        }
    }
}
