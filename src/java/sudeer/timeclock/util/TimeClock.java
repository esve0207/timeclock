/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudeer.timeclock.util;

import java.util.ArrayList;
import sudeer.timeclock.util.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import sudeer.timeclock.util.DbStatements;
import sudeer.timeclock.util.DateUtil;

/**
 *
 * @author sudee
 */
public class TimeClock {

    public static final Logger debugger = Logger.getLogger("sudeer.timeclock");
    //  private ArrayList<Employee> EmpNames;

    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement result = null;
    private ResultSet resultSet = null;
    private String dbDriver = null;
    private String dbConn = null;

//    public static void main(String[] arg) {
//
//        TimeClock tc = new TimeClock(props);
//
//    }

    public TimeClock(Properties props) {
        dbDriver = props.getProperty("db_driver");
        dbConn = props.getProperty("db_connection");
    }


    public int computeHrsWorked(String fromHrs, String toHrs) {
        int hrsWorked = 0;
        int fromH = getAMPMtoIntHrs(fromHrs);
        int toH = getAMPMtoIntHrs(toHrs);
        hrsWorked = (fromH > toH) ? (24 + toH - fromH) : (toH - fromH);
        debugger.info("Worked Hours : " + fromHrs + " to " + toHrs + " -- " + hrsWorked);
        return hrsWorked;
    }

    public int getAMPMtoIntHrs(String szHours) {
        int result = 0;

        switch (szHours) {
            case "1am":
                result = 1;
                break;
            case "2am":
                result = 2;
                break;
            case "3am":
                result = 3;
                break;
            case "4am":
                result = 4;
                break;
            case "5am":
                result = 5;
                break;
            case "6am":
                result = 6;
                break;
            case "7am":
                result = 7;
                break;
            case "8am":
                result = 8;
                break;
            case "9am":
                result = 9;
                break;
            case "10am":
                result = 10;
                break;
            case "11am":
                result = 11;
                break;
            case "12pm":
                result = 12;
                break;
            case "1pm":
                result = 13;
                break;
            case "2pm":
                result = 14;
                break;
            case "3pm":
                result = 15;
                break;
            case "4pm":
                result = 16;
                break;
            case "5pm":
                result = 17;
                break;
            case "6pm":
                result = 18;
                break;
            case "7pm":
                result = 19;
                break;
            case "8pm":
                result = 20;
                break;
            case "9pm":
                result = 21;
                break;
            case "10pm":
                result = 22;
                break;
            case "11pm":
                result = 23;
                break;
            case "0am":
                result = 24;
                break;
        }
        return result;

    }

    /**
     * Creates the connection to the database for the class.
     *
     * @return Connection or null if not created
     */
    public Connection establishConnection() {

        conn = null; //get a new connection

        try {
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbConn);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

        return conn;
    }

    public Employee getEmployee(String name) {

        PreparedStatement getEmpStmt = null;
        ResultSet rs = null;
        Employee resultEmp = null;

        conn = establishConnection();
        try {

            getEmpStmt = conn.prepareStatement(DbStatements.GET_EMPLOYEE_BY_NAME);
            getEmpStmt.setString(1, name);

            rs = getEmpStmt.executeQuery();

            while (rs.next()) {

                String first = rs.getString(DbStatements.EMP_COLUMNS[0]);               
                String pass = rs.getString(DbStatements.EMP_COLUMNS[1]);
                int admin = rs.getInt(DbStatements.EMP_COLUMNS[2]);
                
                resultEmp = new Employee(first, pass, admin);
            }

        } catch (Exception ex) {

            debugger.info("Error in GetEmployee Details " + ex.toString());
        } finally {

            closeQuietly(conn, getEmpStmt, rs);

        }
        return resultEmp;
    }

    //instead of two steps, like update employee table  and clock in table, status can get from clock in table itself.
    //true means..employee already clock in.
    public boolean getEmployeeClockInStatus(String name) {
        boolean status = false;
        PreparedStatement getEmpStmt = null;
        ResultSet rs = null;
        conn = establishConnection();
        try {
            getEmpStmt = conn.prepareStatement(DbStatements.CLOCK_IN_STATUS);
            getEmpStmt.setString(1, name);
            rs = getEmpStmt.executeQuery();
            if (rs.next()) { //resultset returned a row. It means employee is already sign in.
                status = true;
            }
        } catch (Exception ex) {
            debugger.info("Error in GetEmployeeClockInStatus : " + ex.toString());
        } finally {
            closeQuietly(conn, getEmpStmt, rs);
        }
        return status;
    }

    public boolean markAsPaid(int punchId) {
        boolean result = false;
        PreparedStatement setHrsPaid = null;
        conn = establishConnection();
        try {
            setHrsPaid = conn.prepareStatement(DbStatements.MARK_PAID);
            setHrsPaid.setInt(1, punchId);
            int rowsUpdated = setHrsPaid.executeUpdate();
            if (rowsUpdated > 0) {
                result = true;//success. row updated.
            }
            debugger.log(Level.INFO, "Hours with punch_id : {0} marked as PAID. ", punchId);
        } catch (Exception ex) {
            debugger.log(Level.INFO, "Error in Set Hours paid {0}", ex.toString());
        } finally {
            closeQuietly(conn, setHrsPaid);
        }
        return result;
    }

   

    public List<String> getUnpaidHrs(String name) {
        List<String> result = new ArrayList<>();
        PreparedStatement getEmpStmt = null;
        ResultSet rs = null;

        conn = establishConnection();
        try {

            getEmpStmt = conn.prepareStatement(DbStatements.UNPAID_HOURS);
            getEmpStmt.setString(1, name);

            rs = getEmpStmt.executeQuery();

            String temp;
            while (rs.next()) {

                temp = "";

                temp = temp + rs.getInt(DbStatements.CLOCK_TIME_COLUMNS[0]) + "=";
                temp = temp + rs.getTimestamp(DbStatements.CLOCK_TIME_COLUMNS[2]).toString().substring(0, 10) + "=";

                temp = temp + rs.getString(DbStatements.CLOCK_TIME_COLUMNS[3]) + "=";
                //if user is not yet clocked out, clock out hour will be null.

                String clkOutHr = rs.getString(DbStatements.CLOCK_TIME_COLUMNS[5]);
                if (clkOutHr != null) {
                    temp = temp + rs.getString(DbStatements.CLOCK_TIME_COLUMNS[5]) + "=";
                    temp = temp + computeHrsWorked(rs.getString(DbStatements.CLOCK_TIME_COLUMNS[3]), rs.getString(DbStatements.CLOCK_TIME_COLUMNS[5]));
                } else {
                    temp = temp + "nil" + "=";
                    temp = temp + "nil";
                }
                result.add(temp.trim());
            }

        } catch (Exception ex) {

            debugger.info("Error in GetEmployee Details " + ex.toString());
        } finally {

            closeQuietly(conn, getEmpStmt, rs);

        }
        return result;

    }
//1.verify whether employee is already clock in

    //2. update clock in time in clock time table.
    public boolean setEmployeeClockIn(String name, String hours) {
        boolean result = false;
        PreparedStatement setClockIn = null;
        conn = establishConnection();

        debugger.log(Level.INFO, "{0} Stataus is updated to CLOCK IN in employees table.", name);
        try {

            setClockIn = conn.prepareStatement(DbStatements.CLOCK_IN);
            setClockIn.setString(1, name);
            setClockIn.setString(2, hours);

            int rowsUpdated = setClockIn.executeUpdate();

            if (rowsUpdated > 0) {
                result = true;//success. row updated.
            }

        } catch (Exception ex) {
            debugger.log(Level.INFO, "Error in Set employee Clock in hours {0}", ex.toString());
        } finally {
            closeQuietly(conn, setClockIn);
        }

        return result;

    }

    public boolean setEmployeeClockOut(String name, String hours) {
        boolean result = false;
        PreparedStatement setClockOut = null;
        conn = establishConnection();

            try {

                setClockOut = conn.prepareStatement(DbStatements.CLOCK_OUT);
                setClockOut.setString(1, hours);
                setClockOut.setString(2, name);

                int rowsUpdated = setClockOut.executeUpdate();

                if (rowsUpdated > 0) {
                    result = true;//success. row updated.
                }

            } catch (Exception ex) {
                debugger.log(Level.INFO, "Error in Set employee Clock Out hours {0}", ex.toString());
            } finally {
                closeQuietly(conn, setClockOut);
            }
        
        return result;

    }
//    public void readDataBase() throws Exception {
//        try {
//            // This will load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            // Setup the connection with the DB
//            conn = DriverManager
//                    .getConnection("jdbc:mysql://localhost/kig_db?"
//                            + "user=kiran&password=sudeer");
//
//            // Statements allow to issue SQL queries to the database
//            statement = conn.createStatement();
//            // Result set get the result of the SQL query
//            resultSet = statement
//                    .executeQuery("select * from test1");
//            writeResultSet(resultSet);
//
////            // PreparedStatements can use variables and are more efficient
////            preparedStatement = connect
////                    .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
////            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
////            // Parameters start with 1
////            preparedStatement.setString(1, "Test");
////            preparedStatement.setString(2, "TestEmail");
////            preparedStatement.setString(3, "TestWebpage");
////            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
////            preparedStatement.setString(5, "TestSummary");
////            preparedStatement.setString(6, "TestComment");
////            preparedStatement.executeUpdate();
////            
////            preparedStatement = connect
////                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
////            resultSet = preparedStatement.executeQuery();
////            writeResultSet(resultSet);
////
////            // Remove again the insert comment
////            preparedStatement = connect
////            .prepareStatement("delete from feedback.comments where myuser= ? ; ");
////            preparedStatement.setString(1, "Test");
////            preparedStatement.executeUpdate();
////
////            resultSet = statement
////            .executeQuery("select * from feedback.comments");
////            writeMetaData(resultSet);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            close();
//        }
//
//    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String employee_id = resultSet.getString("employee_id");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            Date is_admin = resultSet.getDate("is_admin");
            String pword = resultSet.getString("password");
            String punch_stat = resultSet.getString("punch_stat");
            System.out.println("employee_id: " + employee_id);
            System.out.println("first_name: " + first_name);
            System.out.println("last_name: " + last_name);
            System.out.println("is_admin: " + is_admin);
            System.out.println("punch_stat: " + punch_stat);
        }
    }

    // You need to close the resultSet
    private void closeAll() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Updates the clock status for the employee list.
     *
     * @param emp
     * @param in
     */
//    public void clockEmployee(Employee emp, boolean in) {
//        int empIndex = 0;
//        boolean found = false;
//        while (empIndex < EmpNames.size() && found == false) {
//            if (EmpNames.get(empIndex).getEmployeeID() == emp.getEmployeeID()) {
//                EmpNames.get(empIndex).setClockedIn(in);
//                found = true;
//                debugger.log(Level.INFO, "Updated Clock Status for {0}", EmpNames.get(empIndex).toString());
//            }
//            empIndex++;
//        }
//    }
    /**
     * Close a <code>Connection</code>, avoid closing if null.
     *
     * @param conn Connection to close.
     * @throws SQLException if a database access error occurs
     */
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * Close a <code>ResultSet</code>, avoid closing if null.
     *
     * @param rs ResultSet to close.
     * @throws SQLException if a database access error occurs
     */
    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /**
     * Close a <code>Statement</code>, avoid closing if null.
     *
     * @param stmt Statement to close.
     * @throws SQLException if a database access error occurs
     */
    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    /**
     * Close a <code>Connection</code>, avoid closing if null and hide any
     * SQLExceptions that occur.
     *
     * @param conn Connection to close.
     */
    public static void closeQuietly(Connection conn) {
        try {
            close(conn);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Close a <code>Connection</code>, <code>Statement</code> and
     * <code>ResultSet</code>. Avoid closing if null and hide any SQLExceptions
     * that occur.
     *
     * @param conn Connection to close.
     * @param stmt Statement to close.
     * @param rs ResultSet to close.
     */
    public static void closeQuietly(Connection conn, PreparedStatement stmt,
            ResultSet rs) {

        try {
            closeQuietly(rs);
        } finally {
            try {
                closeQuietly(stmt);
            } finally {
                closeQuietly(conn);
            }
        }

    }

    public static void closeQuietly(Connection conn, PreparedStatement stmt) {

        try {
            closeQuietly(stmt);
        } finally {
            closeQuietly(conn);
        }

    }

    /**
     * Close a <code>ResultSet</code>, avoid closing if null and hide any
     * SQLExceptions that occur.
     *
     * @param rs ResultSet to close.
     */
    public static void closeQuietly(ResultSet rs) {
        try {
            close(rs);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Close a <code>Statement</code>, avoid closing if null and hide any
     * SQLExceptions that occur.
     *
     * @param stmt Statement to close.
     */
    public static void closeQuietly(PreparedStatement stmt) {
        try {
            close(stmt);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Rollback any changes made on the given connection.
     *
     * @param conn Connection to rollback. A null value is legal.
     * @throws SQLException if a database access error occurs
     */
    public static void rollback(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    /**
     * Performs a rollback on the <code>Connection</code> then closes it, avoid
     * closing if null.
     *
     * @param conn Connection to rollback. A null value is legal.
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    public static void rollbackAndClose(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * Performs a rollback on the <code>Connection</code> then closes it, avoid
     * closing if null and hide any SQLExceptions that occur.
     *
     * @param conn Connection to rollback. A null value is legal.
     * @since DbUtils 1.1
     */
    public static void rollbackAndCloseQuietly(Connection conn) {
        try {
            rollbackAndClose(conn);
        } catch (SQLException e) {
            // quiet
        }
    }

}

