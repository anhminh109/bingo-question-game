package com.bingo.servlet;

import com.bingo.dao.QuestionDAO;
import com.bingo.model.Question;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "QuestionServlet", urlPatterns = {"/questions"})
public class QuestionServlet extends HttpServlet {
    private final QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            Map<Integer, Question> questions = questionDAO.getAllQuestions();
            response.getWriter().write(toJson(questions));
        } catch (SQLException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Không thể lấy danh sách câu hỏi.\"}");
        }
    }

    private String toJson(Map<Integer, Question> questions) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Question question : questions.values()) {
            if (!first) {
                json.append(',');
            }
            json.append("{\"cellNumber\":")
                    .append(question.getCellNumber())
                    .append(",\"questionText\":\"")
                    .append(escapeJson(question.getQuestionText()))
                    .append("\"}");
            first = false;
        }

        json.append(']');
        return json.toString();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
