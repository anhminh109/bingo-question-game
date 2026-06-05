package com.bingo.dao;

import com.bingo.model.Question;
import com.bingo.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionDAO {

    public Map<Integer, Question> getAllQuestions() throws SQLException {
        ensureSchema();

        String sql = "SELECT id, cell_number, question_text, answer FROM questions ORDER BY cell_number";
        Map<Integer, Question> questions = createEmptyQuestionMap();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cellNumber = rs.getInt("cell_number");
                questions.put(cellNumber, new Question(
                        rs.getInt("id"),
                        cellNumber,
                        rs.getString("question_text"),
                        rs.getString("answer")
                ));
            }
        }
        return questions;
    }

    public Question getQuestionByCellNumber(int cellNumber) throws SQLException {
        ensureSchema();

        String sql = "SELECT id, cell_number, question_text, answer FROM questions WHERE cell_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cellNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Question(
                            rs.getInt("id"),
                            rs.getInt("cell_number"),
                            rs.getString("question_text"),
                            rs.getString("answer")
                    );
                }
            }
        }
        return new Question(cellNumber, "", "");
    }

    public void saveOrUpdateQuestion(int cellNumber, String questionText, String answer) throws SQLException {
        ensureSchema();

        String sql = "INSERT INTO questions (cell_number, question_text, answer) "
                + "VALUES (?, ?, ?) "
                + "ON CONFLICT (cell_number) DO UPDATE SET "
                + "question_text = EXCLUDED.question_text, "
                + "answer = EXCLUDED.answer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cellNumber);
            ps.setString(2, clean(questionText));
            ps.setString(3, clean(answer));
            ps.executeUpdate();
        }
    }

    public void saveAllQuestions(Map<Integer, String> questionTexts, Map<Integer, String> answers) throws SQLException {
        ensureSchema();

        String sql = "INSERT INTO questions (cell_number, question_text, answer) "
                + "VALUES (?, ?, ?) "
                + "ON CONFLICT (cell_number) DO UPDATE SET "
                + "question_text = EXCLUDED.question_text, "
                + "answer = EXCLUDED.answer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int i = 1; i <= 25; i++) {
                ps.setInt(1, i);
                ps.setString(2, clean(questionTexts.get(i)));
                ps.setString(3, clean(answers.get(i)));
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        }
    }

    public void resetQuestions() throws SQLException {
        ensureSchema();

        String sql = "UPDATE questions SET question_text = '', answer = '' WHERE cell_number BETWEEN 1 AND 25";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }
    }

    private void ensureSchema() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS questions ("
                + "id SERIAL PRIMARY KEY, "
                + "cell_number INT NOT NULL UNIQUE, "
                + "question_text TEXT, "
                + "answer VARCHAR(500)"
                + ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }

        ensureDefaultRows();
    }

    private void ensureDefaultRows() throws SQLException {
        String sql = "INSERT INTO questions (cell_number, question_text, answer) "
                + "VALUES (?, '', '') "
                + "ON CONFLICT (cell_number) DO NOTHING";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 25; i++) {
                ps.setInt(1, i);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private Map<Integer, Question> createEmptyQuestionMap() {
        Map<Integer, Question> questions = new LinkedHashMap<>();
        for (int i = 1; i <= 25; i++) {
            questions.put(i, new Question(i, "", ""));
        }
        return questions;
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}