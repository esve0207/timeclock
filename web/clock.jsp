<%-- 
    Document   : clock
    Created on : Oct 19, 2019, 8:43:05 PM
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
        <bigBluetext>Hi! <%=request.getAttribute("empName")%></bigBluetext><br> <br>
        <bigBluetext>Data updated successfully.</bigBluetext><br><br>
        <bigRedText><%=request.getAttribute("clockMsgName")%></bigRedText><br>
        <bigRedText><%=request.getAttribute("clockMsg")%> at <%=request.getAttribute("clockMsgHrs")%></bigRedText>
        <br><br>
        <bigRedtext>Click <a href="index.html">here</a> to go to Home Page.</bigRedText>
    </center>
    
    
</body>
</html>