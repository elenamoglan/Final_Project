package com.example.demo;

public class Attendance {
    private int employeeId;
    private String date;
    private String status;

    // Constructor
    public Attendance(int employeeId, String date, String status) {
        this.employeeId = employeeId;
        this.date = date;
        this.status = status;
    }


    // Getter for Employee ID
    public int getEmployeeId() {
        return employeeId;
    }

    // Getter for Date
    public String getDate() {
        return date;
    }

    // Getter for Status
    public String getStatus() {
        return status;
    }
}
