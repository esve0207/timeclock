<%-- 
    Document   : error
    Created on : Oct 16, 2019, 3:07:08 PM
    Author     : sudee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>KIG - Clock Time - Error</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="kig.css" rel="stylesheet">
        <script src="kig.js"></script> 
    </head>
    <body>
    <center>
        <h1>Kiran Indian Grocery</h1> 
        <bigRedtext>Oops! Something went wrong.</bigRedtext> <br><br>
        <bigtext> <%=request.getAttribute("errorMsg")%></bigtext><br><br>
        <p align="center"><a href="index.html">Wants to try again?</a></p>
    </center>
    
    
</body>
</html>
