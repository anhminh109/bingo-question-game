<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.bingo.dao.QuestionDAO"%>
<%@page import="com.bingo.model.Question"%>
<%@page import="java.util.Map"%>
<%!
    private String html(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
<%
    QuestionDAO questionDAO = new QuestionDAO();
    Map<Integer, Question> questions = questionDAO.getAllQuestions();
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bingo </title>
        <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/assets/images/bingo.png">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css?v=20260529-2">
    </head>
    <body>
        <div class="app-shell">
            <header class="top-bar">
                <div class="brand">
                    <div class="brand-logo" aria-hidden="true">
                        <span>BINGO</span>
                    </div>
                    <div>
                        <p class="eyebrow">Trò chơi lớp học</p>
                        <h1>Bingo câu hỏi</h1>
                    </div>
                </div>
                <nav class="actions">
                    <a class="button secondary" href="<%= request.getContextPath() %>/setting.jsp"><span class="button-icon">⚙</span>Setting</a>
                    <button class="button" type="button" id="resetGameBtn"><span class="button-icon">↻</span>Reset game</button>
                </nav>
            </header>

            <main class="game-layout">
                <section class="board-section" aria-label="Bảng Bingo 5x5">
                    <div class="bingo-board">
                        <% for (int i = 1; i <= 25; i++) {
                            Question question = questions.get(i);
                            String questionText = question == null ? "" : question.getQuestionText();
                        %>
                        <button class="bingo-cell"
                                type="button"
                                data-cell="<%= i %>"
                                data-question="<%= html(questionText) %>">
                            <%= i %>
                        </button>
                        <% } %>
                    </div>
                </section>

                <aside class="question-panel" aria-live="polite">
                    <div class="question-heading">
                        <div class="question-icon" aria-hidden="true">☷</div>
                        <div>
                            <p class="panel-label">Ô đang chọn</p>
                            <h2 id="questionNumber">Chưa chọn câu hỏi</h2>
                        </div>
                    </div>
                    <div class="question-box is-empty" id="questionText">
                        <span class="empty-mark" aria-hidden="true">?</span>
                        <span>Bấm vào một ô trên bảng Bingo để hiển thị câu hỏi.</span>
                    </div>
                    <div class="hint">
                        <span class="hint-icon" aria-hidden="true">i</span>
                        <span>Các ô đã chọn sẽ được đổi màu. Nút Reset game chỉ xóa đánh dấu trên màn hình, không xóa câu hỏi trong database.</span>
                    </div>
                </aside>
            </main>
        </div>

        <script src="<%= request.getContextPath() %>/assets/js/script.js?v=20260529-2" charset="UTF-8"></script>
    </body>
</html>
