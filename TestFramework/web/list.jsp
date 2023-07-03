<%-- 
    Document   : list
    Created on : 3 juil. 2023, 12:31:42
    Author     : Safidy
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

