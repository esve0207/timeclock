<%-- 
    Document   : paid
    Created on : Oct 22, 2019, 5:39:34 AM
    Author     : sudee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>KIG - Clock Time</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
         <link href="kig.css" rel="stylesheet">
        <script src="kig.js"></script> 
    </head>
    <body>
    <center>
        <h1>Kiran Indian Grocery</h1> 
        <bigRedtext>Status of the PAID hours.</bigRedtext><br> <bigtext> <%=request.getAttribute("paidMsg")%></bigtext><br>
        <p align="center">Click <a href="index.html">here</a> to go to Home Page.</p>
    </center>
    
    
</body>
</html>
