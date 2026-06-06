<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.bingo.util.SessionUtil"%>
<%
    if (SessionUtil.getUserId(request) == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String target = request.getContextPath() + "/settings.jsp";
    if (request.getQueryString() != null) {
        target += "?" + request.getQueryString();
    }

    response.sendRedirect(target);
%>
