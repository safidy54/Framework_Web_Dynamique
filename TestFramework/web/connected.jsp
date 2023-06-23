<%-- 
    Document   : list
    Created on : 11 juin 2023, 22:29:06
    Author     : tiavi
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
