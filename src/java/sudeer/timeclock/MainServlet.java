/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudeer.timeclock;

import sudeer.timeclock.util.TimeClock;
import sudeer.timeclock.util.Mailer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sudeer.timeclock.util.Employee;

/**
 *
 * @author sudee
 */
public class MainServlet extends HttpServlet {

    public static final Logger debugger = Logger.getLogger("sudeer.timeclock");
    String logFile;
    String cfgFile;
    Properties props;
    Handler fileHandler = null;
    TimeClock tc;
    Mailer mailer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        props = new Properties();
        InputStream inputStream = null;
        cfgFile = getServletContext().getInitParameter("cfgFile");
        try {
            inputStream = new FileInputStream(cfgFile);
            props.load(inputStream);
        } catch (FileNotFoundException ex) {
            System.out.println("Error in reading config file : " + ex);
        } catch (Exception ex) {
            System.out.println("Error in reading config file : " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (Exception ex) {
                System.out.println("Error in reading config file : " + ex);
            }
        }

        // Setting up Logger
        debugger.setUseParentHandlers(false);
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s | %2$s | %5$s %6$s%n");

        debugger.setLevel(Level.INFO);

        try {
            logFile = props.getProperty("logFile");
            //  logFile = getServletContext().getInitParameter("logFile");
            System.out.println("**************" + logFile);
            fileHandler = new FileHandler(logFile, true);
            debugger.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());

        } catch (IOException | SecurityException ex) {
            debugger.log(Level.INFO, null, ex);
        }

        debugger.info("log file started.");

        debugger.info("Reading properties file.....SUCCESS.");
        debugger.info(props.toString());

        tc = new TimeClock(props);
        mailer = new Mailer(props);

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String errorMsg = "";
        String empName1 = null;
        String passwd = null;
        String empName2 = null;

        //in order to use the same servlet for all the logic, checking first to see 
        //whether the request came from 'Manage Hours' Page
        String isPaid = request.getParameter("isPaid");
        String paidHrs = request.getParameter("totalHrs");
        String paidName = request.getParameter("paidName");

        if (isPaid != null) {
            if (isPaid.equalsIgnoreCase("update")) {
                String[] IDs = request.getParameterValues("punchId");
                String lstSize = "list size is " + request.getParameter("listSize");

                int successRate = 0;
                // if lstSize and size of IDs arary is same, then all check boxes are selected. 
                //so we need to mark all hrs as PAID.
                for (String id : IDs) {
                    if (tc.markAsPaid(Integer.parseInt(id))) {
                        successRate++;
                    }
                }
                //all checked  hours are marked as PAID.
                String paidMsg = "";
                if (IDs.length == successRate) {
                    paidMsg = "All checked hours are marked as PAID in database successfully.";
                    if (props.getProperty("enableEmail").equalsIgnoreCase("yes")) {
                        try {
                            mailer.send(paidName, "TotalPaidHrs : " + paidHrs);
                        } catch (Exception ex) {
                            debugger.info("PaidHrs : Sending email error :" + ex.toString());
                        }
                    }
                } else {
                    paidMsg = "Not all checked hours are marked as PAID. Please check again.";
                }
                ServletContext sc = getServletConfig().getServletContext();
                request.setAttribute("paidMsg", paidMsg);
                RequestDispatcher rd = sc.getRequestDispatcher("/paid.jsp");
                rd.forward(request, response);
            }
        }
        /**
         * * Beginning of the main logic *
         */

        //Connection conn;
        Employee emp = null;

        empName1 = request.getParameter("empName");
        debugger.info("employee name enetred is " + empName1);

        if (empName1.startsWith("Select")) {
            errorMsg = errorMsg + "\n" + "*** No name is selected." + "\n";
        }
        passwd = request.getParameter("password");
        debugger.info("Password entered is " + passwd);
        if (passwd.length() < 1) {
            errorMsg = errorMsg + "\n" + "*** No password is entered." + "\n";
        }

        if (errorMsg.length() > 2) {
            errorMsg = errorMsg + "\n" + "Please correct the error and resubmit." + "\n";
            ServletContext sc = getServletConfig().getServletContext();
            request.setAttribute("errorMsg", errorMsg);
            RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
        }

        String action = request.getParameter("action");
        debugger.info("action entered is " + action);
        String hours = request.getParameter("hours");
        debugger.info("hours selected is " + hours);

        empName2 = request.getParameter("hrsName");
        debugger.info("HrsName selected is " + empName2);

//get employee password and admin status.
        emp = tc.getEmployee(empName1);
        debugger.info("EMP OBJECT is " + emp.toString());
        //verify if the pin number is correct.
        if (passwd.equalsIgnoreCase(emp.getPassword())) {
            // do nothing
        } else {//then password is wrong.

            errorMsg = "\n" + "Wrong Password entered. Please correct this error and resubmit." + "\n";
            ServletContext sc = getServletConfig().getServletContext();
            request.setAttribute("errorMsg", errorMsg);
            RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);

        }
//check empName1 and empName2 are same. Only ADMIN can modify others records.
        if (empName2.length() > 1) {
            if (empName1.equalsIgnoreCase(empName2)) {
                //do Nothing . User is Self Managing.
            } else { //check whether empName1 is ADMIN
                if (emp.getIsAdmin() < 1) { //it means empName1 is not ADMIN
                    errorMsg = "\n" + "You do not have privileges to manage Other's Time." + "\n";
                    ServletContext sc = getServletConfig().getServletContext();
                    request.setAttribute("errorMsg", errorMsg);
                    RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                }
            }
        }
        // all the error checks are done. Continue to do the main logic.
        //empName1 ---who is doing
        //empName2  -- Action is happening to whoom

        String employee = (empName2.length() > 1) ? empName2 : empName1;

        if (action.equalsIgnoreCase("clockIn")) {

            //verify employee is already clock in.
            if (tc.getEmployeeClockInStatus(employee)) { //then employee is already Clocked in.
                errorMsg = "<br> Hi, " + empName1 + "!  Employee already clocked in. Please correct this error and resubmit." + "\n";
                ServletContext sc = getServletConfig().getServletContext();
                request.setAttribute("errorMsg", errorMsg);
                RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            } else {
                //Logic for clock in action
                //2. update clock in time in clock time table.
                if (tc.setEmployeeClockIn(employee, hours)) {
                    debugger.log(Level.INFO, "{0} Clock in hours {1}got updated.", new Object[]{employee, hours});
                    if (props.getProperty("enableEmail").equalsIgnoreCase("yes")) {
                        try {
                            mailer.send(employee, "ClockedIn : " + hours);
                        } catch (Exception ex) {
                            debugger.info("ClockIn : Sending email error :" + ex.toString());
                        }
                    }
                    ServletContext sc = getServletConfig().getServletContext();
                    request.setAttribute("clockMsg", "Clocked In");
                    request.setAttribute("empName", empName1);
                    request.setAttribute("clockMsgName", employee);
                    request.setAttribute("clockMsgHrs", hours);
                    RequestDispatcher rd = sc.getRequestDispatcher("/clock.jsp");
                    rd.forward(request, response);
                } else {
                    debugger.log(Level.INFO, "FAILED to update Clock In hours for {1}", employee);

                    errorMsg = "<br> Hi, " + empName1 + "!  Clock in is FAILED to get recorded. Please contact SUDEER." + "\n";
                    ServletContext sc = getServletConfig().getServletContext();
                    request.setAttribute("errorMsg", errorMsg);
                    RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                }
            }
        } else if (action.equalsIgnoreCase("clockOut")) {

            //Logic for clock out action
            //verify whether the employee is already clockinout
            //2. update clock out time in clock time table.
            if (!(tc.getEmployeeClockInStatus(employee))) { //then employee is not Clocked in.
                errorMsg = "<br> Hi, " + empName1 + "!  Employee NOT clocked in. Please correct this error and resubmit." + "\n";
                ServletContext sc = getServletConfig().getServletContext();
                request.setAttribute("errorMsg", errorMsg);
                RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            } else {
                if (tc.setEmployeeClockOut(employee, hours)) {
                    debugger.log(Level.INFO, "{0} Clock out hours {1}got updated.", new Object[]{employee, hours});
                    if (props.getProperty("enableEmail").equalsIgnoreCase("yes")) {
                    try {
                        mailer.send(employee, "ClockedOut : " + hours);
                    } catch (Exception ex) {
                        debugger.info("ClockIn : Sending email error :" + ex.toString());
                    }
                    }
                    ServletContext sc = getServletConfig().getServletContext();
                    request.setAttribute("clockMsg", "Clocked Out");
                    request.setAttribute("empName", empName1);
                    request.setAttribute("clockMsgName", employee);
                    request.setAttribute("clockMsgHrs", hours);
                    RequestDispatcher rd = sc.getRequestDispatcher("/clock.jsp");
                    rd.forward(request, response);
                } else {
                    debugger.log(Level.INFO, "FAILED to update Clock Out hours for {1}", employee);
                    errorMsg = "<br> Hi, " + empName1 + "!  Clock Out is FAILED to get recorded. Please contact SUDEER." + "\n";
                    ServletContext sc = getServletConfig().getServletContext();
                    request.setAttribute("errorMsg", errorMsg);
                    RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                }
            }
        } else if (action.equalsIgnoreCase("manageTime")) {
            //Manage Time Logic
            //1. check empName1 and HrsName are same.
            //if not same, check whether empName1 is ADMIN to view other hours.
            //hrsName = request.getParameter("empName2");

            // by this stage, all the checks are done. we can proceed with manage time logic.
            // get all the unpaid hours from clock in table.            
            List<String> unPaidHrs = tc.getUnpaidHrs(employee);
            debugger.info("List of Unpaid hours.." + unPaidHrs);
            ServletContext sc = getServletConfig().getServletContext();
            request.setAttribute("hrsName", employee);
            request.setAttribute("unpaidHrs", unPaidHrs);
            RequestDispatcher rd = sc.getRequestDispatcher("/manage.jsp");
            rd.forward(request, response);
        } else {
            errorMsg = "\n" + "Not possible to reach this call flow. Please contact SUDEER." + "\n";
            ServletContext sc = getServletConfig().getServletContext();
            request.setAttribute("errorMsg", errorMsg);
            RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
        }

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
