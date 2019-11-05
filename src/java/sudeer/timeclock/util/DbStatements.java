/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudeer.timeclock.util;

/**
 *
 * @author sudee
 */
import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 * Wrapper class that contains all SQL statements being used in this
 * application.
 *
 * @author demont-imac
 */
public class DbStatements {

    private static HashMap<String, PreparedStatement> statements;

    /* Table names */
    public static final String[] TABLE_NAMES = {"EMPLOYEES", "CLOCK_TIME"};
    /* Tables
    
    CREATE table KIG_DB.EMPLOYEES (
            FIRST_NAME VARCHAR(20) NOT NULL PRIMARY KEY, 
             PASSWORD VARCHAR(10) NOT NULL,
             IS_ADMIN SMALLINT DEFAULT 0);
    
    
    */
    
        public static final String[] EMP_COLUMNS = {"FIRST_NAME",
         "PASSWORD", "IS_ADMIN"};
//    public static final String CREATE_EMPLOYEES = "CREATE table APP.EMPLOYEES ("
//            + "EMPLOYEE_ID INT NOT NULL "
//            + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
//            + "(START WITH 1001, INCREMENT BY 1), "
//            + "FIRST_NAME VARCHAR(30), "
//            + "LAST_NAME VARCHAR(30), "
//            + "IS_SALARY SMALLINT DEFAULT 0, "
//            + "IS_ADMIN SMALLINT DEFAULT 0, "
//            + "PASSWORD VARCHAR(64) NOT NULL,"
//            + "PUNCH_STAT SMALLINT DEFAULT 0) ";

  /***
   * CREATE TABLE KIG_DB.CLOCK_TIME (
    punch_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    clock_in_hour VARCHAR(4) not null,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    clock_out_hour VARCHAR(4), 
    IS_PAID SMALLINT DEFAULT 0); 
    * ***/
 /* Time punch table */
    public static final String[] CLOCK_TIME_COLUMNS = {"PUNCH_ID", "EMP_NAME" ,"CREATED_AT",
        "CLOCK_IN_HOUR", "UPDATED_AT", "CLOCK_OUT_HOUR", "IS_PAID"};
    

//    public static final String[] CREATE_TABLE_SQL = {
//        DbStatements.CREATE_EMPLOYEES,
//        DbStatements.CLOCK_TIME};

 


public static final String GET_EMPLOYEE_PASSWORD = "SELECT password "
            + "FROM KIG_DB.EMPLOYEES WHERE FIRST_NAME=?";
    public static final String GET_EMPLOYEE_BY_NAME = "SELECT * "
            + "FROM KIG_DB.EMPLOYEES WHERE FIRST_NAME=?";


   

    // to find whether the employee is already CLOCKED IN
    //resultSet returns 1 row if the employee is already clock in
    //resultset returns 0 if the employee is not clocked in 
    public static final String CLOCK_IN_STATUS = "select clock_in_hour from KIG_DB.CLOCK_TIME "
            + "where emp_name= ? and clock_out_hour IS NULL";
    // Update table with employee clock in time
    public static final String CLOCK_IN = "INSERT INTO KIG_DB.CLOCK_TIME "
            + "(EMP_NAME,CLOCK_IN_HOUR) VALUES(?,?)";
    // insert into KIG_DB.CLOCK_TIME_3 (emp_name,clock_in_hour) values ('test1',1900); 
    // update KIG_DB.CLOCK_TIME_3 set clock_out_hour=1800 where emp_name='test1' and clock_out_hour is null;
    //Update table with employee clock out time
    public static final String CLOCK_OUT = "UPDATE KIG_DB.CLOCK_TIME "
            + "SET CLOCK_OUT_HOUR = ? WHERE EMP_NAME = ? AND CLOCK_OUT_HOUR IS NULL";
    //select * from   KIG_DB.CLOCK_TIME where emp_name='test1' and is_paid = 0;
    public static final String UNPAID_HOURS = "select punch_id, created_at,clock_in_hour,clock_out_hour from   KIG_DB.CLOCK_TIME where emp_name= ? and is_paid = 0";
//update KIG_DB.CLOCK_TIME set is_paid=1 where punch_id=1;
    public static final String MARK_PAID="update KIG_DB.CLOCK_TIME set is_paid=1 where punch_id= ?"; 
}

/***
 * 
 * CREATE table KIG_DB.EMPLOYEES (
            FIRST_NAME VARCHAR(20) NOT NULL PRIMARY KEY, 
             PASSWORD VARCHAR(10) NOT NULL,
             IS_ADMIN SMALLINT DEFAULT 0);
 * 
 
    * 
    * 
    * 
    * 
    * CREATE TABLE KIG_DB.CLOCK_TIME (
    punch_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    clock_in_hour VARCHAR(4) not null,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    clock_out_hour VARCHAR(4), 
    IS_PAID SMALLINT DEFAULT 0);
    
  select * from   KIG_DB.CLOCK_TIME;
    insert into KIG_DB.CLOCK_TIME (emp_name, clock_in_hour) values ('test1',1600);
 
 update KIG_DB.CLOCK_TIME set clock_out_hour=1800 where emp_name='test1' and clock_out_hour is null;
    select * from kig_db.employees;          
              select * from   KIG_DB.CLOCK_TIME;
insert into KIG_DB.CLOCK_TIME (emp_name,clock_in_hour) values ('test2','10am');            
update KIG_DB.CLOCK_TIME set clock_out_hour='8pm' where emp_name='test1' and clock_out_hour is null;
update KIG_DB.CLOCK_TIME set is_paid=1 where punch_id=1;
select clock_in_hour from KIG_DB.CLOCK_TIME where emp_name='test1' and clock_out_hour IS NULL;
select punch_id, created_at,clock_in_hour,clock_out_hour from   KIG_DB.CLOCK_TIME where emp_name='test1' and is_paid = 0;


update kig_db.employees set is_clock_in=0 where first_name='test1';

CREATE TABLE KIG_DB.CLOCK_TIME (
    punch_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    clock_in_hour VARCHAR(4) not null,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    clock_out_hour VARCHAR(4), 
    IS_PAID SMALLINT DEFAULT 0); 
    * 
);
            * 
            * 
            * 
            * 
            * 
 */
