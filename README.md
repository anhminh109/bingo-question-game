# Bingo Question Game - Servlet/JSP/SQL Server

Project Web Java chạy trên Apache Tomcat, dùng Servlet/JSP, HTML/CSS/JavaScript và Microsoft SQL Server. Ứng dụng gồm bảng Bingo 5x5, mỗi ô tương ứng một câu hỏi, có trang Setting để quản lý 25 câu hỏi.

## Cấu trúc thư mục chính

```text
Bingo/
|-- database/
|   |-- bingo_game.sql
|-- src/java/com/bingo/
|   |-- dao/QuestionDAO.java
|   |-- model/Question.java
|   |-- servlet/QuestionServlet.java
|   |-- servlet/SaveQuestionServlet.java
|   |-- servlet/ResetServlet.java
|   |-- util/DBConnection.java
|-- web/
|   |-- assets/css/style.css
|   |-- assets/js/script.js
|   |-- WEB-INF/web.xml
|   |-- index.jsp
|   |-- setting.jsp
|-- build.xml
```

## Tạo database SQL Server

1. Mở SQL Server Management Studio.
2. Kết nối vào SQL Server của bạn.
3. Chọn `File` > `Open` > `File`.
4. Mở file `database/bingo_game.sql`.
5. Bấm `Execute`.

Script sẽ tạo:

```text
Database: bingo_game
Table: questions
- id INT IDENTITY(1,1) PRIMARY KEY
- cell_number INT UNIQUE NOT NULL
- question_text NVARCHAR(MAX)
```

## Cấu hình kết nối SQL Server

Mở file `src/java/com/bingo/util/DBConnection.java`:

```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=bingo_game;encrypt=true;trustServerCertificate=true";
private static final String USERNAME = "sa";
private static final String PASSWORD = "your_password";
```

Bạn sửa:

- `localhost:1433`: địa chỉ và port SQL Server.
- `USERNAME`: tài khoản SQL Server.
- `PASSWORD`: mật khẩu SQL Server.

Nếu SQL Server của bạn chưa bật port `1433`, mở `SQL Server Configuration Manager`, bật `TCP/IP`, đặt port `1433`, rồi restart SQL Server service.

## Thêm SQL Server JDBC Driver vào project

1. Tải Microsoft JDBC Driver for SQL Server, file thường có tên dạng `mssql-jdbc-12.x.x.jre11.jar`.
2. Trong NetBeans, click phải project `Bingo`.
3. Chọn `Properties`.
4. Chọn `Libraries`.
5. Ở tab `Compile`, bấm `Add JAR/Folder`.
6. Chọn file `mssql-jdbc-12.x.x.jre11.jar`.
7. Bấm `OK`.

Khi build WAR, NetBeans sẽ copy thư viện vào `WEB-INF/lib`.

## Import project vào NetBeans

1. Mở NetBeans.
2. Chọn `File` > `Open Project`.
3. Chọn thư mục `Bingo`.
4. Bấm `Open Project`.
5. Nếu NetBeans hỏi server, chọn Apache Tomcat.

## Cấu hình Tomcat

1. Vào `Tools` > `Servers`.
2. Bấm `Add Server`.
3. Chọn `Apache Tomcat or TomEE`.
4. Trỏ tới thư mục cài Tomcat.
5. Project này đang dùng Tomcat 10, nên code Servlet dùng package `jakarta.servlet.*`.

Nếu bạn dùng Tomcat 9, hãy đổi import `jakarta.servlet.*` trong các Servlet sang `javax.servlet.*` và đổi `web.xml` sang schema Java EE tương ứng.

## Chạy project

1. Đảm bảo SQL Server đang chạy.
2. Chạy script `database/bingo_game.sql` trong SQL Server Management Studio.
3. Sửa username/password trong `DBConnection.java`.
4. Thêm `mssql-jdbc` vào Libraries của project.
5. Click phải project `Bingo`.
6. Chọn `Clean and Build`.
7. Chọn `Run`.
8. Trình duyệt mở địa chỉ tương tự:

```text
http://localhost:8080/Bingo/
```

## Cách sử dụng

- Trang chính `index.jsp`: hiển thị bảng Bingo 5x5.
- Bấm một ô bất kỳ để đổi màu ô và hiện câu hỏi bên phải.
- Bấm `Reset game` để bỏ đánh dấu các ô đã chọn trên giao diện.
- Bấm `Setting` để vào trang quản lý câu hỏi.
- Trang `setting.jsp`: nhập/sửa câu hỏi cho 25 ô, bấm `Lưu câu hỏi` để lưu database.
- Bấm `Reset câu hỏi về rỗng` để xóa nội dung 25 câu hỏi trong database.

## Servlet endpoint

- `GET /questions`: trả danh sách câu hỏi dạng JSON.
- `POST /save-questions`: lưu/cập nhật toàn bộ 25 câu hỏi.
- `POST /reset-questions`: reset toàn bộ câu hỏi về rỗng.
"# bingo-question-game" 
