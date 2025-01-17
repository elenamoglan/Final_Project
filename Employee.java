package com.example.demo;

import java.io.*;
import java.util.*;

public class Employee implements Serializable {
    private String name;
    private int employeeId;
    private double salary;
    private int departmentId;
    private String email;

    public Employee(int employeeId, String name, int departmentId, double salary, String email) {
        this.name = name;
        this.employeeId = employeeId;
        this.salary = salary;
        this.departmentId = departmentId;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return employeeId;
    }

    public void setId(int id) {
        this.employeeId = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartment(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
