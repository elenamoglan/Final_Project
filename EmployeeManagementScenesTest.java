package com.example.demo;

import javafx.application.Platform;
import org.junit.Test;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeManagementScenesTest {
    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Start the JavaFX application thread
        Platform.startup(() -> {});
    }

    @Test
    public void testConstructorDatabaseConnection() {
        assertDoesNotThrow(() -> {
            DatabaseConnection connection = new DatabaseConnection();
        });
    }

    @Test
    public void testDatabaseConnectionForAdmin() {
        assertDoesNotThrow(() -> {
            Connection connection = DatabaseConnection.getConnection("admin");
            assertNotNull(connection, "Connection should not be null for admin");
        });
    }

    @Test
    public void testAuthenticateAdminSuccess() {
        EmployeeManagementApplication app = new EmployeeManagementApplication();
        assertTrue(app.authenticate("admin", "admin_password"), "Admin authentication should succeed");
    }

    @Test
    public void testAuthenticateManagerFailure() {
        EmployeeManagementApplication app = new EmployeeManagementApplication();
        assertFalse(app.authenticate("manager", "wrongPassword"), "Manager authentication should fail");
    }

    @Test
    public void testAuthenticateEmployeeSuccess() {
        EmployeeManagementApplication app = new EmployeeManagementApplication();
        assertTrue(app.authenticate("employee", "employee_password"), "Employee authentication should succeed");
    }

    @Test
    public void testAuthenticateInvalidUser() {
        EmployeeManagementApplication app = new EmployeeManagementApplication();
        assertFalse(app.authenticate("invalid", "password"), "Invalid user authentication should fail");
    }

}
