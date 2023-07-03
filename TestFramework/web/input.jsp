<%-- 
    Document   : input
    Created on : 3 juil. 2023, 12:29:44
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
        <h1>Hello World!</h1>
        <form action="save.action" method="post" enctype="multipart/form-data">
            <input type="text" name="firstname" value="Safidy">
            <input type="text" name="lastname" value="Rabezatovo">
            <input type="file" name="myfiles">
            <input type="checkbox" name="loisir" value="foot">
            <input type="submit" value="valideo ary">
        </form>
        <br>
        <form action="saveS.action" method="post" enctype="multipart/form-data">
            <input type="text" name="firstname" value="Safidy">
            <input type="text" name="lastname" value="Rabezatovo">
            <input type="file" name="myfiles">
            <input type="submit" value="valideo ary">
        </form>

    </body>
</html>
