<%-- 
    Document   : input
    Created on : 5 mai 2023, 07:42:04
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
        <h1>Formulaire</h1>
        <form action="save" method="post">
            <input type="text" name="nom" value="Rah">
            <input type="text" name="prenom" value="Safidy">
            <input type="date" name="dtn" >
            <input type="submit" value="Envoyer">

        </form>
    </body>
</html>
