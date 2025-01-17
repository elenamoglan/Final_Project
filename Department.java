package com.example.demo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Department implements Serializable {
    private int departmentId;
    private String name;
    //private static Set<Employee> employees;

    public Department(int departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
        //this.employees = new HashSet<Employee>();
    }
    public int getDepartmentId() {
        return departmentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
//    public Set<Employee> getEmployees() {
//        return employees;
//    }
//    public void setEmployees(Set<Employee> employees) {
//        this.employees = employees;
//    }

//    public void addEmployee(Employee employee) {
//        if (!employees.contains(employee)) {
//            employees.add(employee);
//        } else {
//            System.out.println("Employee with ID" + employee.getId() + "already exists");
//        }
//    }

//    public void removeEmployee(Employee employee) {
//        employees.remove(employee);
//    }

}

