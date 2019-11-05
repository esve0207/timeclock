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
public class Employee {

    private String EmployeeFirstName;
    private String Password;
    private int IsAdmin;

    public Employee() {
        this.EmployeeFirstName = null;
        this.Password = null;
        this.IsAdmin = 0;
    }

    public Employee(String first, String pass, int isAdmin) {

        this.EmployeeFirstName = first;

        this.Password = pass;
        this.IsAdmin = isAdmin;

    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return (EmployeeFirstName != null) ? EmployeeFirstName : "(Not Set)";
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return (Password != null) ? Password : "(Not Set)";
    }

    /**
     *
     * @return
     */
    public int getIsAdmin() {
        return IsAdmin;
    }

    /**
     * Implements the toString method of the Employee object, prints employee
     * id, first name, and last name.
     *
     * @return
     */
    @Override
    public String toString() {
        return getFirstName()+"|"+getPassword()+"|"+getIsAdmin();
    }

    /**
     * Implements compare for Collections.sort method, sorts by first name then
     * by last name.
     *
     * @param compareEmp the Employee being compared to this Employee
     */
//    @Override
//    public int compareTo(Employee compareEmp) {
//        int result;
//        String firstName1 = this.getFirstName().toUpperCase();
//        String firstName2 = compareEmp.getFirstName().toUpperCase();
//        result = firstName1.compareTo(firstName2);
//        if (result == 0) {
//            String lastName1 = this.getLastName().toUpperCase();
//            String lastName2 = compareEmp.getLastName().toUpperCase();
//            result = lastName1.compareTo(lastName2);
//        }
//        return result;
//    }
}
