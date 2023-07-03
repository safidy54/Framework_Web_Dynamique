<%-- 
    Document   : non_connected
    Created on : 3 juil. 2023, 12:34:56
    Author     : Safidy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p><%= session.getAttribute("isconnected") %></p>
        <p><%= session.getAttribute("profile") %></p>
        <h1>Mot de passe erroné ou utilisateur inconnue</h1>
        <a href="log.action">Retour</a>
        <br>

    </body>
</html>
