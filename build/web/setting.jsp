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
    boolean saved = "1".equals(request.getParameter("saved"));
    boolean reset = "1".equals(request.getParameter("reset"));
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý câu hỏi Bingo</title>
        <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/assets/images/bingo.png">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css?v=20260530-answer-slide">
    </head>
    <body>
        <div class="app-shell">
            <header class="top-bar">
                <div>
                    <p class="eyebrow">Thiết lập trò chơi</p>
                    <h1>Quản lý câu hỏi</h1>
                </div>
                <nav class="actions">
                    <a class="button secondary" href="<%= request.getContextPath() %>/index.jsp">Về bảng Bingo</a>
                </nav>
            </header>

            <% if (saved) { %>
            <div class="notice success">Đã lưu toàn bộ câu hỏi và đáp án vào database.</div>
            <% } %>
            <% if (reset) { %>
            <div class="notice warning">Đã reset nội dung 25 câu hỏi và đáp án về rỗng.</div>
            <% } %>

            <main class="setting-layout">
                <form class="question-form" action="<%= request.getContextPath() %>/save-questions" method="post">
                    <div class="question-list">
                        <% for (int i = 1; i <= 25; i++) {
                            Question question = questions.get(i);
                            String questionText = question == null ? "" : question.getQuestionText();
                            String answer = question == null ? "" : question.getAnswer();
                        %>
                        <div class="question-item">
                            <span>Ô <%= i %></span>

                            <label for="question_<%= i %>">Câu hỏi</label>
                            <textarea id="question_<%= i %>"
                                      name="question_<%= i %>"
                                      rows="3"
                                      placeholder="Nhập câu hỏi cho ô <%= i %>"><%= html(questionText) %></textarea>

                            <label for="answer_<%= i %>">Đáp án</label>
                            <textarea id="answer_<%= i %>"
                                      name="answer_<%= i %>"
                                      rows="2"
                                      placeholder="Nhập đáp án cho ô <%= i %>"><%= html(answer) %></textarea>
                        </div>
                        <% } %>
                    </div>

                    <div class="form-actions">
                        <button class="button save" type="submit">Lưu câu hỏi và đáp án</button>
                    </div>
                </form>

                <form class="reset-form" action="<%= request.getContextPath() %>/reset-questions" method="post"
                      onsubmit="return confirm('Bạn có chắc muốn xóa nội dung của 25 câu hỏi và đáp án?');">
                    <button class="button danger" type="submit">Reset câu hỏi và đáp án về rỗng</button>
                </form>
            </main>
        </div>
    </body>
</html>
