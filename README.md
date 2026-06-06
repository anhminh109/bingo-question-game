# Bingo Question Game - JSP/Servlet/PostgreSQL

Ứng dụng Bingo 5x5 chạy trên Tomcat 10, dùng JSP/Servlet Jakarta và PostgreSQL.
Mỗi tài khoản có bộ 25 câu hỏi/đáp án riêng.

## Cấu trúc chính

```text
Bingo/
|-- database/
|   |-- bingo_game.sql
|   |-- add_answer_column.sql
|-- src/java/com/bingo/
|   |-- dao/QuestionDAO.java
|   |-- dao/UserDAO.java
|   |-- model/Question.java
|   |-- model/User.java
|   |-- servlet/LoginServlet.java
|   |-- servlet/LogoutServlet.java
|   |-- servlet/QuestionServlet.java
|   |-- servlet/SaveQuestionServlet.java
|   |-- servlet/ResetServlet.java
|   |-- util/DBConnection.java
|   |-- util/PasswordService.java
|   |-- util/SessionUtil.java
|-- web/
|   |-- assets/css/style.css
|   |-- assets/js/script.js
|   |-- WEB-INF/web.xml
|   |-- index.jsp
|   |-- login.jsp
|   |-- settings.jsp
|   |-- setting.jsp
|-- build.xml
```

## Database

Chạy `database/bingo_game.sql` trong PostgreSQL. Script tạo:

- `users(id, username, password)`
- `questions(id, user_id, cell_number, question_text, answer)`
- unique index theo `(user_id, cell_number)`
- user mẫu `admin/admin123`
- 25 dòng câu hỏi rỗng cho user `admin`

Project lấy chuỗi kết nối từ biến môi trường `DB_URL`.

Ví dụ:

```text
postgresql://username:password@localhost:5432/bingo_game
```

hoặc:

```text
jdbc:postgresql://localhost:5432/bingo_game
```

## Chạy project

1. Tạo database PostgreSQL.
2. Chạy `database/bingo_game.sql`.
3. Đặt biến môi trường `DB_URL`.
4. Mở project bằng NetBeans, chọn Tomcat 10.
5. Clean and Build, rồi Run.

URL thường gặp:

```text
http://localhost:8080/Bingo/
```

## Đăng nhập

User demo:

```text
username: admin
password: admin123
```

Nếu chưa đăng nhập, `index.jsp`, `settings.jsp` và các servlet dữ liệu sẽ redirect về `login.jsp`.

## Endpoint

- `GET /questions`: trả danh sách câu hỏi của user đang đăng nhập.
- `POST /save-questions`: lưu 25 câu hỏi/đáp án của user đang đăng nhập.
- `POST /reset-questions`: reset 25 câu hỏi/đáp án của user đang đăng nhập.
- `GET/POST /logout`: đăng xuất session hiện tại.
