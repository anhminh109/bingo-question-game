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
        ensureDefaultRows();

        String sql = "SELECT id, cell_number, question_text FROM questions ORDER BY cell_number";
        Map<Integer, Question> questions = createEmptyQuestionMap();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cellNumber = rs.getInt("cell_number");
                questions.put(cellNumber, new Question(
                        rs.getInt("id"),
                        cellNumber,
                        rs.getString("question_text")
                ));
            }
        }

        return questions;
    }

    public Question getQuestionByCellNumber(int cellNumber) throws SQLException {
        ensureDefaultRows();

        String sql = "SELECT id, cell_number, question_text FROM questions WHERE cell_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cellNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Question(
                            rs.getInt("id"),
                            rs.getInt("cell_number"),
                            rs.getString("question_text")
                    );
                }
            }
        }

        return new Question(cellNumber, "");
    }

    public void saveOrUpdateQuestion(int cellNumber, String questionText) throws SQLException {
        String sql = "MERGE questions AS target "
                + "USING (SELECT ? AS cell_number, ? AS question_text) AS source "
                + "ON target.cell_number = source.cell_number "
                + "WHEN MATCHED THEN UPDATE SET question_text = source.question_text "
                + "WHEN NOT MATCHED THEN INSERT (cell_number, question_text) "
                + "VALUES (source.cell_number, source.question_text);";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cellNumber);
            ps.setString(2, questionText == null ? "" : questionText.trim());
            ps.executeUpdate();
        }
    }

    public void saveAllQuestions(Map<Integer, String> questionTexts) throws SQLException {
        String sql = "MERGE questions AS target "
                + "USING (SELECT ? AS cell_number, ? AS question_text) AS source "
                + "ON target.cell_number = source.cell_number "
                + "WHEN MATCHED THEN UPDATE SET question_text = source.question_text "
                + "WHEN NOT MATCHED THEN INSERT (cell_number, question_text) "
                + "VALUES (source.cell_number, source.question_text);";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (int i = 1; i <= 25; i++) {
                ps.setInt(1, i);
                ps.setString(2, questionTexts.getOrDefault(i, "").trim());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        }
    }

    public void resetQuestions() throws SQLException {
        String sql = "UPDATE questions SET question_text = '' WHERE cell_number BETWEEN 1 AND 25";

        ensureDefaultRows();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }
    }

    private void ensureDefaultRows() throws SQLException {
        String sql = "MERGE questions AS target "
                + "USING (SELECT ? AS cell_number, '' AS question_text) AS source "
                + "ON target.cell_number = source.cell_number "
                + "WHEN NOT MATCHED THEN INSERT (cell_number, question_text) "
                + "VALUES (source.cell_number, source.question_text);";

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
            questions.put(i, new Question(i, ""));
        }
        return questions;
    }
}
