<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.bingo.dao.QuestionDAO"%>
<%@page import="com.bingo.model.Question"%>
<%@page import="com.bingo.util.SessionUtil"%>
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
    Integer userId = SessionUtil.getUserId(request);
    if (userId == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String username = SessionUtil.getUsername(request);
    QuestionDAO questionDAO = new QuestionDAO();
    Map<Integer, Question> questions = questionDAO.getAllQuestions(userId);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bingo</title>
        <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/assets/images/bingo.png">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css?v=20260607-guide">
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
                    <span class="user-pill"><%= html(username) %></span>
                    <a class="button secondary compact" href="<%= request.getContextPath() %>/settings.jsp"><span class="button-icon">⚙</span>Setting</a>
                    <button class="button secondary compact guide-button" type="button" id="openGuideBtn"
                            aria-haspopup="dialog" aria-controls="guideModal">
                        <span class="button-icon guide-button-icon" aria-hidden="true">?</span>Hướng dẫn chơi
                    </button>
                    <button class="button compact" type="button" id="resetGameBtn"><span class="button-icon">↻</span>Reset game</button>
                    <a class="button secondary compact" href="<%= request.getContextPath() %>/logout">Logout</a>
                </nav>
            </header>

            <main class="game-layout">
                <section class="board-section" aria-label="Bảng Bingo 5x5">
                    <div class="bingo-board">
                        <% for (int i = 1; i <= 25; i++) {
                            Question question = questions.get(i);
                            String questionText = question == null ? "" : question.getQuestionText();
                            String answer = question == null ? "" : question.getAnswer();
                        %>
                        <button class="bingo-cell"
                                type="button"
                                data-cell="<%= i %>"
                                data-question="<%= html(questionText) %>"
                                data-answer="<%= html(answer) %>">
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

                    <button class="answer-toggle" type="button" id="showAnswerBtn" disabled>
                        Hiển thị đáp án
                    </button>

                    <div class="answer-box" id="answerBox" aria-hidden="true">
                        <div class="answer-content">
                            <span class="answer-label">Đáp án</span>
                            <p id="answerText"></p>
                        </div>
                    </div>

                    <div class="hint">
                        <span class="hint-icon" aria-hidden="true">i</span>
                        <span>Chuột phải vào ô đã chọn để bỏ chọn nếu bấm nhầm. Nút Reset game chỉ xóa đánh dấu trên màn hình, không xóa dữ liệu trong database.</span>
                    </div>
                </aside>
            </main>
        </div>

        <div class="modal-backdrop" id="guideModal" role="dialog" aria-modal="true"
             aria-labelledby="guideTitle" aria-hidden="true">
            <section class="guide-modal" tabindex="-1">
                <button class="modal-close" type="button" id="closeGuideBtn" aria-label="Đóng hướng dẫn">×</button>

                <div class="guide-modal-header">
                    <span class="guide-modal-icon" aria-hidden="true">?</span>
                    <h2 id="guideTitle">Hướng dẫn chơi Bingo</h2>
                </div>

                <ol class="guide-list">
                    <li>Người chơi sẽ trả lời các ô trên bảng Bingo 5x5.</li>
                    <li>Mỗi ô sẽ tương ứng với một câu hỏi.</li>
                    <li>Trả lời đúng thì ô được chọn 1 con số.</li>
                    <li>Ai hoàn thành 5 hàng ngang, hàng dọc hoặc đường chéo trước sẽ thắng và phải hô BINGO!!!</li>
                    <li>Có thể dùng nút hiển thị đáp án nếu người quản trò cần kiểm tra.</li>
                </ol>

                <div class="guide-actions">
                    <button class="button compact" type="button" id="confirmGuideBtn">Đã hiểu</button>
                </div>
            </section>
        </div>

        <script src="<%= request.getContextPath() %>/assets/js/script.js?v=20260607-guide" charset="UTF-8"></script>
    </body>
</html>
