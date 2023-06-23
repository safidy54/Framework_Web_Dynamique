<%-- 
    Document   : list
    Created on : 11 juin 2023, 22:29:06
    Author     : safidy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="save.action" method="post" enctype="multipart/form-data">
            <input type="text" name="firstname" value="Tiavina">
            <input type="text" name="lastname" value="Malalaniaina">
            <input type="file" name="myfiles">
            <input type="checkbox" name="loisir" value="foot">
            <input type="checkbox" name="loisir" value="basket">
            <input type="checkbox" name="loisir" value="volley">
            <input type="submit" value="send">
        </form>
        <br>
        <form action="saveS.action" method="post" enctype="multipart/form-data">
            <input type="text" name="firstname" value="Tiavina">
            <input type="text" name="lastname" value="Malalaniaina">
            <input type="file" name="myfiles">
            <input type="submit" value="send">
        </form>

    </body>
</html>
