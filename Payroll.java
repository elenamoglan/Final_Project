package com.example.demo;

public class Payroll {
    private int id;
    private int employeeId;
    private String payPeriod ;
    private int daysWorked;
    private double calculatedSalary;

    // Constructor
    public Payroll(int employeeId, String payPeriod, int daysWorked ) {
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.daysWorked = daysWorked;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Getter for Employee ID
    public int getEmployeeId() {
        return employeeId;
    }

    // Getter for Days Worked
    public int getDaysWorked() {
        return daysWorked;
    }

    // Getter for Calculated Salary
    public double getCalculatedSalary() {
        return calculatedSalary;
    }

    public String getPayPeriod() {
        return payPeriod;
    }
}
