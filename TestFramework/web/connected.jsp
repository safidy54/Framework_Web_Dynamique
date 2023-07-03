<%-- 
    Document   : connected
    Created on : 3 juil. 2023, 12:28:07
    Author     : Safidy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% String user = String.valueOf(request.getAttribute("user")); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1> Vous etes connecter en tant que <%= user %> <h1>
        <br>

    </body>
</html>
