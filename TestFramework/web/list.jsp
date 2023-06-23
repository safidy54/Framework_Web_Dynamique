<%-- 
    Document   : list
    Created on : 11 juin 2023, 22:29:06
    Author     : tiavi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String first_name = String.valueOf(request.getAttribute("first_name"));    
    String last_name = String.valueOf(request.getAttribute("last_name"));
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <ul>
            <li><%= first_name %></li>
            <li><%= last_name %></li>
        </ul>
    </body>
</html>
