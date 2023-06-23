<%-- 
    Document   : index
    Created on : 5 mai 2023, 07:40:33
    Author     : Safidy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String nom = String.valueOf(request.getAttribute("nom"));
    String prenom = String.valueOf(request.getAttribute("prenom"));
    Object dtn = request.getAttribute("dtn");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Testing Model View</h1>
        <p><%= nom %></p>
        <p><%= prenom %></p>
        <p><%= dtn %></p>
    </body>
</html>
