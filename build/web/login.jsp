<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.bingo.util.SessionUtil"%>
<%
    if (SessionUtil.getUserId(request) != null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    boolean error = "1".equals(request.getParameter("error"));
    boolean logout = "1".equals(request.getParameter("logout"));
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập Bingo</title>
        <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/assets/images/bingo.png">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css?v=20260606-user-questions">
    </head>
    <body>
        <main class="login-shell">
            <section class="login-card">
                <div class="brand login-brand">
                    <div class="brand-logo" aria-hidden="true">
                        <span>BINGO</span>
                    </div>
                    <div>
                        <p class="eyebrow">Tài khoản riêng</p>
                        <h1>Bingo</h1>
                    </div>
                </div>

                <% if (error) { %>
                <div class="notice warning">Sai username hoặc password.</div>
                <% } %>
                <% if (logout) { %>
                <div class="notice success">Bạn đã đăng xuất.</div>
                <% } %>

                <form class="login-form" action="<%= request.getContextPath() %>/login" method="post">
                    <label for="username">Username</label>
                    <input id="username" name="username" type="text" autocomplete="username" required autofocus>

                    <label for="password">Password</label>
                    <input id="password" name="password" type="password" autocomplete="current-password" required>

                    <button class="button login-button" type="submit">Đăng nhập</button>
                </form>
            </section>
        </main>
    </body>
</html>
