package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.*;

public class ValidationUtils {
    static boolean isValidId(int id) {
        return id > 0;
    }

    // Validate if choice is within a valid range
    public static boolean isValidChoice(int choice, int min, int max) {
        return choice >= min && choice <= max;
    }

    static boolean isValidSalary(double salary) {
        return salary > 0;
    }

    static boolean isValidAttendanceStatus(String status) {
        return status.equalsIgnoreCase("Present") || status.equalsIgnoreCase("Absent");
    }

    static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z0-9 _-]+$");
    }

    static boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // Parse the date string using the provided format
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Validate that date is not in the future
    public static boolean isDateInPastOrToday(String input) {
        try {
            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static int validateWorkingDays(int input) {
        int daysWorked = input;
        try {
            if (daysWorked < 1 || daysWorked > 31) {
                throw new IllegalArgumentException("Working days must be between 1 and 31.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input. Please enter a numeric value.");
        }
        return daysWorked;
    }

    public static boolean isValidPayPeriod(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            formatter.parse(input); // Parse without appending "-01"
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean doesEmployeeExist(Connection connection, int employeeId) {
        String query = "SELECT 1 FROM Employee WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // True if the employee exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking employee existence: " + e.getMessage());
        }
        return false;
    }

    public static boolean doesEmployeeExistByEmail(Connection connection, String email) {
        String query = "SELECT 1 FROM Employee WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // True if a duplicate exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking duplicate employee: " + e.getMessage());
        }
        return false;
    }

    public static boolean doesDepartmentExist(Connection connection, int departmentId) {
        String query = "SELECT 1 FROM Department WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // True if the department exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking department existence: " + e.getMessage());
        }
        return false;
    }

//    public static boolean doesDepartmentExistByName(Connection connection, String name) {
//        String query = "SELECT 1 FROM Department WHERE name = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, name);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                return resultSet.next(); // True if a duplicate exists
//            }
//        } catch (SQLException e) {
//            System.err.println("Error checking duplicate department: " + e.getMessage());
//        }
//        return false;
//    }

    public static boolean doesAttendanceExist(Connection connection, int employeeId, String date) {
        String query = "SELECT 1 FROM Attendance WHERE employee_id = ? AND date = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // True if the attendance record exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking attendance existence: " + e.getMessage());
        }
        return false;
    }

    public static boolean doesPayrollExist(Connection connection, int employeeId, String payPeriod) {
        String query = "SELECT 1 FROM Payroll WHERE employee_id = ? AND pay_period = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, payPeriod);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // True if the payroll record exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking payroll existence: " + e.getMessage());
        }
        return false;
    }
}
