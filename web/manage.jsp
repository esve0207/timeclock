<%-- 
    Document   : manage
    Created on : Oct 21, 2019, 7:10:19 PM
    Author     : sudee
--%>
<%@page import="java.util.List"%>
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
        <bigRedtext>Clock In/Out details for  <%=request.getAttribute("hrsName")%></bigRedtext><br> <br>

        <form action="/TimeClock/MainServlet" method="POST"> 
            <input type="hidden" name="isPaid" value="update"></p>
            <input type="hidden" name="paidName" value=" <%=request.getAttribute("hrsName")%> "></p>


            <table id="dbtable">
                <tr>
                    <th>id</th>
                    <th>Date</th>
                    <th>ClockIn</th>
                    <th>ClockOut</th>
                    <th>Hours</th>
                    <th>Is Paid</th>
                </tr>


                <%
                    List<String> summaryReport = (List<String>) request.getAttribute("unpaidHrs");
                    int lstSize = summaryReport.size();

                    out.println("<input type='hidden' name='listSize' value=" + lstSize + ">");

                    int totalUnPaidHrs = 0;
                    for (String line : summaryReport) {
                        line.trim();
                        String[] tokens = line.split("=");
                        out.println("<tr><td>" + tokens[0] + "</td>");
                        out.println("<td>" + tokens[1] + "</td>");
                        out.println("<td>" + tokens[2] + "</td>");

                        if (tokens[3].equalsIgnoreCase("nil")) {
                            out.println("<td>nil</td>");
                            out.println("<td>nil</td>");

                        } else {
                            out.println("<td>" + tokens[3] + "</td>");
                            out.println("<td>" + tokens[4] + "</td>");
                            out.println("<td><input type='checkbox' name='punchId' value='" + tokens[0] + "'></td></tr>");
                            totalUnPaidHrs = totalUnPaidHrs + Integer.parseInt(tokens[4]);
                        }
                        
                        
                    }

                    out.println("<bigBluetext>Total unpaid hours : " + totalUnPaidHrs + "</bigBluetext><br><br>");
                    out.println("<input type='hidden' name='totalHrs' value='"+totalUnPaidHrs +"'>");
                %>

            </table>
            <br><br>
            <input type="submit" class="button" value="Update">
        </form>
        <br><br>
        <bigRedtext>Click <a href="index.html">here</a> to go to Home Page.</bigRedText>
    </center>


</body>
</html>