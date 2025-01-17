package com.example.demo;

import java.sql.*;

public class DatabaseConnection {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employees_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Elena2004";

    // Admin credentials
    private static String ADMIN_USER = "admin_user";
    private static String ADMIN_PASSWORD = "admin_password";

    // Manager credentials
    private static final String MANAGER_USER = "manager_user";
    private static final String MANAGER_PASSWORD = "manager_password";

    // Employee credentials
    private static String EMPLOYEE_USER = "employee_user";
    private static String EMPLOYEE_PASSWORD = "employee_password";

    // Get a connection to the database
    public static Connection getConnection(String role) throws SQLException {
        switch (role.toLowerCase()) {
            case "admin":
                return DriverManager.getConnection(DB_URL, ADMIN_USER, ADMIN_PASSWORD);
            case "manager":
                return DriverManager.getConnection(DB_URL, MANAGER_USER, MANAGER_PASSWORD);
            case "employee":
                return DriverManager.getConnection(DB_URL, EMPLOYEE_USER, EMPLOYEE_PASSWORD);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public static Employee getEmployeeById(Connection connection, int employeeId) throws SQLException {
        String query = "SELECT id, name, department_id, salary, email FROM Employee WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int departmentId = resultSet.getInt("department_id");
                    double salary = resultSet.getDouble("salary");
                    String email = resultSet.getString("email");

                    return new Employee(id, name, departmentId, salary, email);
                }
            }
        }
        return null;
    }

    public static Department getDepartmentById(Connection connection, int departmentId) throws SQLException {
        String query = "SELECT * FROM Department WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Department(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
        }
        return null; // Return null if no department is found
    }

    public static Attendance getAttendanceById(Connection connection, int employeeId, String date) throws SQLException {
        String query = "SELECT * FROM Attendance WHERE employee_id = ? AND date = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Attendance(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("date"),
                            resultSet.getString("status")
                    );
                }
            }
        }
        return null; // Return null if no attendance record is found
    }

    public static Payroll getPayrollById(Connection connection, int employeeId, String payPeriod) throws SQLException {
        String query = "SELECT * FROM Payroll WHERE employee_id = ? AND pay_period = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, payPeriod);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Payroll(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("pay_period"),
                            resultSet.getInt("days_worked")
                    );
                }
            }
        }
        return null; // Return null if no payroll record is found
    }

    public static void fetchAttendanceByEmployeeId(Connection connection, int employeeId, StringBuilder attendanceData) throws SQLException {
        String query = "SELECT date, status FROM attendance WHERE employee_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId); // Use the integer employee ID
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                attendanceData.append("Date: ").append(resultSet.getString("date"))
                        .append(", Status: ").append(resultSet.getString("status"))
                        .append("\n");
            }
        }
    }

    public static void fetchPayrollByEmployeeId(Connection connection, int employeeId, StringBuilder payrollData) throws SQLException {
        String query = "SELECT pay_period, days_worked, calculated_salary FROM payroll WHERE employee_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId); // Use the integer employee ID
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                payrollData.append("Pay Period: ").append(resultSet.getString("pay_period"))
                        .append(", Days Worked: ").append(resultSet.getInt("days_worked"))
                        .append(", Calculated Salary: ").append(resultSet.getDouble("calculated_salary"))
                        .append("\n");
            }
        }
    }


    // Fetch employee data
    public static void fetchAndDisplayEmployees(Connection connection, StringBuilder result) {
        String query = "SELECT e.id, e.name, d.name AS department, e.salary, e.email " +
                "FROM Employee e LEFT JOIN Department d ON e.department_id = d.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String department = resultSet.getString("department") != null ? resultSet.getString("department") : "N/A";
                double salary = resultSet.getDouble("salary");
                String email = resultSet.getString("email");

                result.append(String.format("ID: %d | Name: %s | Dept: %s | Salary: %.2f | Email: %s\n",
                        id, name, department, salary, email));
            }
        } catch (SQLException e) {
            result.append("Error fetching employee data: ").append(e.getMessage()).append("\n");
        }
    }

    // Fetch and display departments
    public static void fetchAndDisplayDepartments(Connection connection, StringBuilder result) {
        String query = "SELECT * FROM Department";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                result.append(String.format("ID: %d | Name: %s\n", id, name));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching department data: " + e.getMessage());
        }
    }

    // Fetch and display employee details by ID
    public static void fetchAndDisplayEmployeeDetails(Connection connection, int employeeId, StringBuilder details) {
        String query = "SELECT e.id, e.name, d.name AS department, e.salary, e.email " +
                "FROM Employee e " +
                "LEFT JOIN Department d ON e.department_id = d.id WHERE e.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    details.append("ID: ").append(resultSet.getInt("id")).append("\n")
                            .append("Name: ").append(resultSet.getString("name")).append("\n")
                            .append("Department: ").append(resultSet.getString("department")).append("\n")
                            .append("Salary: ").append(resultSet.getDouble("salary")).append("\n")
                            .append("Email: ").append(resultSet.getString("email")).append("\n");
                } else {
                    details.append("No details found for Employee ID: ").append(employeeId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee details: " + e.getMessage());
        }
    }

    public static void fetchAndDisplayAttendance(Connection connection, StringBuilder result) {
        String query = "SELECT a.id, e.name, a.date, a.status " +
                "FROM Attendance a " +
                "JOIN Employee e ON a.employee_id = e.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");

                result.append(String.format("ID: %d | Name: %s | Date: %s | Status: %s\n", id, name, date, status));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching attendance data: " + e.getMessage());
        }
    }

    public static void fetchAndDisplayPayroll(Connection connection, StringBuilder result) {
        String query = "SELECT p.id, e.name, p.days_worked, p.pay_period, p.calculated_salary " +
                "FROM Payroll p " +
                "JOIN Employee e ON p.employee_id = e.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String employeeName = resultSet.getString("name");
                int daysWorked = resultSet.getInt("days_worked");
                String payPeriod = resultSet.getString("pay_period");
                double calculatedSalary = resultSet.getDouble("calculated_salary");

                result.append(String.format("ID: %d | Name: %s | Days Worked: %d | Pay Period: %s | Calculated Salary: %.2f\n", id, employeeName, daysWorked, payPeriod, calculatedSalary));

            }
        } catch (SQLException e) {
            System.err.println("Error fetching payroll data: " + e.getMessage());
        }
    }

    public static void insertDepartment(Connection connection, String name) {
        String query = "INSERT INTO Department (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted into Department.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: A department with this name already exists.");
            } else {
                System.err.println("Error adding employee: " + e.getMessage());
            }
        }
    }

    public static void insertEmployee(Connection connection, String name, int departmentId, double salary, String email) {
        String query = "INSERT INTO Employee (name, department_id, salary, email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, departmentId);
            statement.setDouble(3, salary);
            statement.setString(4, email);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted into Employee.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: An employee with this email already exists.");
            } else {
                System.err.println("Error adding employee: " + e.getMessage());
            }
        }
    }

    public static void insertAttendance(Connection connection, int employeeId, String date, String status) {
        String query = "INSERT INTO Attendance (employee_id, date, status) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, date);
            statement.setString(3, status);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) added to Attendance.");
        } catch (SQLException e) {
            System.err.println("Error marking attendance: " + e.getMessage());
        }
    }

    public static void insertPayroll(Connection connection, int employeeId, String payPeriod, int daysWorked) {
        String query = "INSERT INTO Payroll (employee_id, days_worked, pay_period) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setInt(2, daysWorked);
            statement.setString(3, payPeriod);  // Specify pay period (e.g., '2025-01')

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) added to Payroll.");
        } catch (SQLException e) {
            System.err.println("Error adding payroll: " + e.getMessage());
        }
    }

    public static void updateEmployee(Connection connection, int employeeId, String newName, int newDepartmentId, double newSalary, String newEmail) {
        String query = "UPDATE Employee SET name = ?, department_id = ?, salary = ?, email = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newName);
            statement.setInt(2, newDepartmentId);
            statement.setDouble(3, newSalary);
            statement.setString(4, newEmail);
            statement.setInt(5, employeeId);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in Employee.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: An employee with this email already exists.");
            } else {
                System.err.println("Error adding employee: " + e.getMessage());
            }
        }
    }

    public static void updateDepartment(Connection connection, int departmentId, String newName) {
        String query = "UPDATE Department SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newName);
            statement.setInt(2, departmentId);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in Department.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: A department with this name already exists.");
            } else {
                System.err.println("Error adding employee: " + e.getMessage());
            }
        }
    }

    public static void updateAttendance(Connection connection, int employeeId, String date, String newStatus) {
        String query = "UPDATE Attendance SET status = ? WHERE employee_id = ? AND date = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newStatus);
            statement.setInt(2, employeeId);
            statement.setString(3, date);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in Attendance.");
        } catch (SQLException e) {
            System.err.println("Error updating attendance: " + e.getMessage());
        }
    }

    public static void updatePayroll(Connection connection, int employeeId, String payPeriod, int newDaysWorked) {
        String query = "UPDATE Payroll SET days_worked = ? WHERE employee_id = ? AND pay_period = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newDaysWorked);
            statement.setInt(2, employeeId);
            statement.setString(3, payPeriod);  // Specify pay period (e.g., '2025-01')

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in Payroll.");
        } catch (SQLException e) {
            System.err.println("Error updating payroll: " + e.getMessage());
        }
    }

    public static void deleteEmployee(Connection connection, int employeeId) {
        String query = "DELETE FROM Employee WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);

            // First, delete related payroll and attendance records
            deleteRelatedPayroll(connection, employeeId);
            deleteRelatedAttendance(connection, employeeId);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted from Employee.");
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }

    // Delete related payroll records for an employee
    public static void deleteRelatedPayroll(Connection connection, int employeeId) {
        String query = "DELETE FROM Payroll WHERE employee_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.executeUpdate();
            System.out.println("Related payroll records deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting related payroll: " + e.getMessage());
        }
    }

    public static void deletePayroll(Connection connection, int employeeId, String payPeriod) {
        String query = "DELETE FROM Payroll WHERE employee_id = ? AND pay_period = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, payPeriod);  // Specify pay period (e.g., '2025-01')

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payroll for Employee ID " + employeeId + " in " + payPeriod + " deleted.");
            } else {
                System.out.println("No payroll found for Employee ID " + employeeId + " in " + payPeriod);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting payroll: " + e.getMessage());
        }
    }

    // Delete related attendance records for an employee
    public static void deleteRelatedAttendance(Connection connection, int employeeId) {
        String query = "DELETE FROM Attendance WHERE employee_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.executeUpdate();
            System.out.println("Related attendance records deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting related attendance: " + e.getMessage());
        }
    }

    public static void deleteAttendance(Connection connection, int employeeId, String date) {
        String query = "DELETE FROM Attendance WHERE employee_id = ? AND date = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);  // Set employee ID
            statement.setString(2, date);     // Set the specific date for the attendance

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Attendance for Employee ID " + employeeId + " on " + date + " deleted.");
            } else {
                System.out.println("No attendance found for Employee ID " + employeeId + " on " + date);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting attendance: " + e.getMessage());
        }
    }

    public static void deleteDepartment(Connection connection, int departmentId) {
        String query = "DELETE FROM Department WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // First, delete employees associated with this department
            deleteRelatedEmployees(connection, departmentId);

            statement.setInt(1, departmentId);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted from Department.");
        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
        }
    }

    // Delete employees associated with a department
    private static void deleteRelatedEmployees(Connection connection, int departmentId) {
        String query = "DELETE FROM Employee WHERE department_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentId);
            statement.executeUpdate();
            System.out.println("Related employees deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting related employees: " + e.getMessage());
        }
    }

}
