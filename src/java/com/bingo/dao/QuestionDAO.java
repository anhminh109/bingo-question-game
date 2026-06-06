package com.bingo.dao;

import com.bingo.model.Question;
import com.bingo.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionDAO {
    private final UserDAO userDAO = new UserDAO();

    public Map<Integer, Question> getAllQuestions(int userId) throws SQLException {
        ensureSchema();
        ensureDefaultRows(userId);

        String sql = "SELECT id, cell_number, question_text, answer "
                + "FROM questions "
                + "WHERE user_id = ? "
                + "ORDER BY cell_number";
        Map<Integer, Question> questions = createEmptyQuestionMap();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
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
        }
        return questions;
    }

    public Question getQuestionByCellNumber(int userId, int cellNumber) throws SQLException {
        ensureSchema();
        ensureDefaultRows(userId);

        String sql = "SELECT id, cell_number, question_text, answer "
                + "FROM questions "
                + "WHERE user_id = ? AND cell_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, cellNumber);
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

    public void saveOrUpdateQuestion(int userId, int cellNumber, String questionText, String answer) throws SQLException {
        ensureSchema();

        String sql = "INSERT INTO questions (user_id, cell_number, question_text, answer) "
                + "VALUES (?, ?, ?, ?) "
                + "ON CONFLICT (user_id, cell_number) DO UPDATE SET "
                + "question_text = EXCLUDED.question_text, "
                + "answer = EXCLUDED.answer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, cellNumber);
            ps.setString(3, clean(questionText));
            ps.setString(4, clean(answer));
            ps.executeUpdate();
        }
    }

    public void saveAllQuestions(int userId, Map<Integer, String> questionTexts, Map<Integer, String> answers)
            throws SQLException {

        ensureSchema();

        String sql = "INSERT INTO questions (user_id, cell_number, question_text, answer) "
                + "VALUES (?, ?, ?, ?) "
                + "ON CONFLICT (user_id, cell_number) DO UPDATE SET "
                + "question_text = EXCLUDED.question_text, "
                + "answer = EXCLUDED.answer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            try {
                for (int i = 1; i <= 25; i++) {
                    ps.setInt(1, userId);
                    ps.setInt(2, i);
                    ps.setString(3, clean(questionTexts.get(i)));
                    ps.setString(4, clean(answers.get(i)));
                    ps.addBatch();
                }

                ps.executeBatch();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public void resetQuestions(int userId) throws SQLException {
        ensureSchema();
        ensureDefaultRows(userId);

        String sql = "UPDATE questions "
                + "SET question_text = '', answer = '' "
                + "WHERE user_id = ? AND cell_number BETWEEN 1 AND 25";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private void ensureSchema() throws SQLException {
        userDAO.ensureSchema();

        String sql = "CREATE TABLE IF NOT EXISTS questions ("
                + "id SERIAL PRIMARY KEY, "
                + "user_id INT NOT NULL REFERENCES users(id), "
                + "cell_number INT NOT NULL, "
                + "question_text TEXT, "
                + "answer VARCHAR(500)"
                + ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }

        ensureMigrationColumns();
        assignExistingRowsToAdmin();
        dropLegacyCellNumberUniqueConstraint();
        ensureUserForeignKey();
        ensureUserCellUniqueIndex();
        setUserIdNotNull();
    }

    public void ensureDefaultRows(int userId) throws SQLException {
        String sql = "INSERT INTO questions (user_id, cell_number, question_text, answer) "
                + "VALUES (?, ?, '', '') "
                + "ON CONFLICT (user_id, cell_number) DO NOTHING";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 25; i++) {
                ps.setInt(1, userId);
                ps.setInt(2, i);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private void ensureMigrationColumns() throws SQLException {
        executeSql("ALTER TABLE questions ADD COLUMN IF NOT EXISTS user_id INT");
        executeSql("ALTER TABLE questions ADD COLUMN IF NOT EXISTS question_text TEXT");
        executeSql("ALTER TABLE questions ADD COLUMN IF NOT EXISTS answer VARCHAR(500)");
    }

    private void assignExistingRowsToAdmin() throws SQLException {
        int adminUserId = userDAO.getAdminUserId();
        String sql = "UPDATE questions SET user_id = ? WHERE user_id IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminUserId);
            ps.executeUpdate();
        }
    }

    private void dropLegacyCellNumberUniqueConstraint() throws SQLException {
        executeSql(
                "DO $$ "
                + "DECLARE constraint_record record; "
                + "BEGIN "
                + "FOR constraint_record IN "
                + "SELECT con.conname "
                + "FROM pg_constraint con "
                + "JOIN pg_class rel ON rel.oid = con.conrelid "
                + "JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace "
                + "WHERE rel.relname = 'questions' "
                + "AND nsp.nspname = current_schema() "
                + "AND con.contype = 'u' "
                + "AND array_length(con.conkey, 1) = 1 "
                + "AND ("
                + "SELECT att.attname FROM pg_attribute att "
                + "WHERE att.attrelid = rel.oid AND att.attnum = con.conkey[1]"
                + ") = 'cell_number' "
                + "LOOP "
                + "EXECUTE format('ALTER TABLE questions DROP CONSTRAINT %I', constraint_record.conname); "
                + "END LOOP; "
                + "END $$"
        );
    }

    private void ensureUserForeignKey() throws SQLException {
        executeSql(
                "DO $$ "
                + "BEGIN "
                + "IF NOT EXISTS ("
                + "SELECT 1 FROM pg_constraint "
                + "WHERE conname = 'questions_user_id_fkey' "
                + "AND conrelid = 'questions'::regclass"
                + ") THEN "
                + "ALTER TABLE questions "
                + "ADD CONSTRAINT questions_user_id_fkey "
                + "FOREIGN KEY (user_id) REFERENCES users(id); "
                + "END IF; "
                + "END $$"
        );
    }

    private void ensureUserCellUniqueIndex() throws SQLException {
        executeSql("CREATE UNIQUE INDEX IF NOT EXISTS uq_questions_user_cell "
                + "ON questions (user_id, cell_number)");
    }

    private void setUserIdNotNull() throws SQLException {
        executeSql("ALTER TABLE questions ALTER COLUMN user_id SET NOT NULL");
    }

    private void executeSql(String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(sql);
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
