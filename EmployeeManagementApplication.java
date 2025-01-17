package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmployeeManagementApplication extends Application {

    private Stage primaryStage;
    private Connection connection;
    private String currentUserRole;
    private ExecutorService clientThreadPool;
    private ExecutorService taskThreadPool;

    public static void main(String[] args) {
        runTests();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        clientThreadPool = Executors.newFixedThreadPool(5); // Limit to 5 clients
        taskThreadPool = Executors.newCachedThreadPool();
        initializeDatabaseConnection();
        showLoginScene();

//        // Simulate multiple clients with different roles
//        String[] roles = {"admin", "manager", "employee"};
//        for (int i = 1; i <= 5; i++) {
//            String role = roles[i % roles.length]; // Assign roles cyclically
//            clientThreadPool.submit(new ClientSimulator(i, role));
//        }
    }

    @Override
    public void stop() {
        clientThreadPool.shutdown();
        taskThreadPool.shutdown();
    }

    private void runAsync(Runnable task) {
        clientThreadPool.submit(task);
    }

    private void initializeDatabaseConnection() {
        try {
            connection = DatabaseConnection.getConnection("admin"); // Default role; adjust as needed
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            showAlert("Database Error", "Unable to connect to the database: " + e.getMessage());
            System.exit(1);
        }
    }

    private void showLoginScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);

        loginButton.setDefaultButton(true);

        loginButton.setOnAction(event -> {
            taskThreadPool.submit(() -> {
                String username = usernameField.getText();
                String password = passwordField.getText();

                if (authenticate(username, password)) {
                    currentUserRole  = determineRole(username);
                    Platform.runLater(() -> {
                        switch (currentUserRole) {
                            case "admin" -> showAdminDashboard();
                            case "manager" -> showManagerDashboard();
                            case "employee" -> showEmployeeDashboard();
                            default -> showAlert("Login Failed", "Invalid role.");
                        }
                    });
                } else {
                    Platform.runLater(() -> showAlert("Login Failed", "Invalid username or password."));
                }
            });
        });

        Scene loginScene = new Scene(grid, 300, 200);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    private void showAdminDashboard() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button viewEmployeesButton = new Button("View Employees");
        Button viewDepartmentsButton = new Button("View Departments");
        Button addEmployeeButton = new Button("Add Employee");
        Button addDepartmentButton = new Button("Add Department");
        Button updateEmployeeButton = new Button("Update Employee");
        Button updateDepartmentButton = new Button("Update Department");
        Button deleteEmployeeButton = new Button("Delete Employee");
        Button deleteDepartmentButton = new Button("Delete Department");
        Button logoutButton = new Button("Logout");

        viewEmployeesButton.setOnAction(event -> showViewEmployeesScene());
        viewDepartmentsButton.setOnAction(event -> showViewDepartmentsScene());
        addEmployeeButton.setOnAction(event -> showAddEmployeeScene());
        addDepartmentButton.setOnAction(event -> showAddDepartmentScene());
        updateEmployeeButton.setOnAction(event -> UpdateEmployeeScene());
        updateDepartmentButton.setOnAction(event -> UpdateDepartmentScene());
        deleteEmployeeButton.setOnAction(event -> DeleteEmployeeScene());
        deleteDepartmentButton.setOnAction(event -> DeleteDepartmentScene());
        logoutButton.setOnAction(event -> showLoginScene());

        layout.getChildren().addAll(viewEmployeesButton, viewDepartmentsButton, addEmployeeButton, addDepartmentButton,
                updateEmployeeButton, updateDepartmentButton, deleteEmployeeButton, deleteDepartmentButton, logoutButton);

        Scene adminScene = new Scene(layout, 300, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Dashboard");
    }

    private void showManagerDashboard() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button viewEmployeesButton = new Button("View Employees");
        Button viewDepartmentsButton = new Button("View Departments");
        Button viewAttendanceButton = new Button("View Attendance");
        Button viewPayrollButton = new Button("View Payroll");
        Button addAttendanceButton = new Button("Add Attendance");
        Button addPayrollButton = new Button("Add Payroll");
        Button updateAttendanceButton = new Button("Update Attendance");
        Button updatePayrollButton = new Button("Update Payroll");
        Button deleteAttendanceButton = new Button("Delete Attendance");
        Button deletePayrollButton = new Button("Delete Payroll");
        Button logoutButton = new Button("Logout");

        viewEmployeesButton.setOnAction(event -> showViewEmployeesScene());
        viewDepartmentsButton.setOnAction(event -> showViewDepartmentsScene());
        viewAttendanceButton.setOnAction(event -> showViewAttendanceScene());
        viewPayrollButton.setOnAction(event -> showViewPayrollScene());
        addAttendanceButton.setOnAction(event -> showAddAttendanceScene());
        addPayrollButton.setOnAction(event -> showAddPayrollScene());
        updateAttendanceButton.setOnAction(event -> UpdateAttendanceScene());
        updatePayrollButton.setOnAction(event -> UpdatePayrollScene());
        deleteAttendanceButton.setOnAction(event -> DeleteAttendanceScene());
        deletePayrollButton.setOnAction(event -> DeletePayrollScene());
        logoutButton.setOnAction(event -> showLoginScene());

        layout.getChildren().addAll(viewEmployeesButton, viewDepartmentsButton, viewAttendanceButton, viewPayrollButton,
                addAttendanceButton, addPayrollButton, updateAttendanceButton, updatePayrollButton, deleteAttendanceButton, deletePayrollButton, logoutButton);

        Scene managerScene = new Scene(layout, 300, 400);
        primaryStage.setScene(managerScene);
        primaryStage.setTitle("Manager Dashboard");
    }

    private void showEmployeeDashboard() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button viewDepartmentsButton = new Button("View Departments");
        Button viewMyAttendanceButton = new Button("View My Attendance");
        Button viewMyPayrollButton = new Button("View My Payroll");
        Button viewMyDetailsButton = new Button("View My Details");
        Button logoutButton = new Button("Logout");

        viewDepartmentsButton.setOnAction(event -> showViewDepartmentsScene());
        viewMyAttendanceButton.setOnAction(event -> showViewMyAttendanceScene());
        viewMyPayrollButton.setOnAction(event -> showViewMyPayrollScene());
        viewMyDetailsButton.setOnAction(event -> showViewMyDetailsScene());
        logoutButton.setOnAction(event -> showLoginScene());

        layout.getChildren().addAll(viewDepartmentsButton, viewMyAttendanceButton, viewMyPayrollButton, viewMyDetailsButton, logoutButton);

        Scene employeeScene = new Scene(layout, 300, 400);
        primaryStage.setScene(employeeScene);
        primaryStage.setTitle("Employee Dashboard");
    }

    private void showViewMyAttendanceScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Button fetchButton = new Button("Fetch Attendance");
        TextArea attendanceArea = new TextArea();
        attendanceArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showEmployeeDashboard());

        fetchButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error", "Employee with ID " + employeeId + " does not exist.");
                    return;
                }

                StringBuilder attendanceDetails = new StringBuilder();

                // Fetch the attendance details based on the entered employee ID
                DatabaseConnection.fetchAttendanceByEmployeeId(connection, employeeId, attendanceDetails);
                attendanceArea.setText(attendanceDetails.toString());
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid Employee ID.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to fetch attendance records: " + e.getMessage());
            }
        });

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(fetchButton, 1, 1);
        grid.add(attendanceArea, 0, 2, 2, 1);
        grid.add(backButton, 1, 3);

        Scene viewMyAttendanceScene = new Scene(grid, 400, 300);
        primaryStage.setScene(viewMyAttendanceScene);
    }


    private void showViewMyPayrollScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Button fetchButton = new Button("Fetch Payroll");
        TextArea payrollArea = new TextArea();
        payrollArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showEmployeeDashboard());

        fetchButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error", "Employee with ID " + employeeId + " does not exist.");
                    return;
                }

                StringBuilder payrollDetails = new StringBuilder();

                // Fetch the payroll details based on the entered employee ID
                DatabaseConnection.fetchPayrollByEmployeeId(connection, employeeId, payrollDetails);
                payrollArea.setText(payrollDetails.toString());
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid Employee ID.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to fetch payroll records: " + e.getMessage());
            }
        });

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(fetchButton, 1, 1);
        grid.add(payrollArea, 0, 2, 2, 1);
        grid.add(backButton, 1, 3);

        Scene viewMyPayrollScene = new Scene(grid, 400, 300);
        primaryStage.setScene(viewMyPayrollScene);
    }

    private void showViewMyDetailsScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Button fetchButton = new Button("Fetch Details");
        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showEmployeeDashboard());

        fetchButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error", "Employee with ID " + employeeId + " does not exist.");
                    return;
                }

                StringBuilder employeeDetails = new StringBuilder();

                // Fetch the employee details based on the entered employee ID
                DatabaseConnection.fetchAndDisplayEmployeeDetails(connection, employeeId, employeeDetails);
                detailsArea.setText(employeeDetails.toString());
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid Employee ID.");
            }
        });

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(fetchButton, 1, 1);
        grid.add(detailsArea, 0, 2, 2, 1);
        grid.add(backButton, 1, 3);

        Scene viewMyDetailsScene = new Scene(grid, 400, 300);
        primaryStage.setScene(viewMyDetailsScene);
    }


    private void showViewEmployeesScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label label = new Label("Employee List:");
        TextArea employeeList = new TextArea();
        employeeList.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigateBackToDashboard());

        taskThreadPool.submit(() -> {
            try {
                StringBuilder employees = new StringBuilder();
                DatabaseConnection.fetchAndDisplayEmployees(connection, employees);
                Platform.runLater(() -> employeeList.setText(employees.toString()));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch employees: " + e.getMessage()));
            }
        });

        layout.getChildren().addAll(label, employeeList, backButton);

        Scene viewEmployeesScene = new Scene(layout, 400, 400);
        primaryStage.setScene(viewEmployeesScene);
    }

    private void showViewDepartmentsScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label label = new Label("Department List:");
        TextArea departmentList = new TextArea();
        departmentList.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigateBackToDashboard());

        try {
            StringBuilder departments = new StringBuilder();
            DatabaseConnection.fetchAndDisplayDepartments(connection, departments);
            departmentList.setText(departments.toString());
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch departments: " + e.getMessage());
        }

        layout.getChildren().addAll(label, departmentList, backButton);

        Scene viewDepartmentScene = new Scene(layout, 400, 400);
        primaryStage.setScene(viewDepartmentScene);
    }

    private void showAddEmployeeScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label departmentLabel = new Label("Department ID:");
        TextField departmentField = new TextField();
        Label salaryLabel = new Label("Salary:");
        TextField salaryField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(departmentLabel, 0, 1);
        grid.add(departmentField, 1, 1);
        grid.add(salaryLabel, 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(submitButton, 1, 4);
        grid.add(backButton, 1, 5);

        backButton.setOnAction(event -> navigateBackToDashboard());

        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            if (!ValidationUtils.isValidName(name)) {
                throw new IllegalArgumentException("Invalid employee name.");
            }

            int departmentId = Integer.parseInt(departmentField.getText());
            if (!ValidationUtils.isValidId(departmentId)) {
                showAlert("Error","Invalid department ID. Please enter a positive integer.");
                return;
            }

            double salary = Double.parseDouble(salaryField.getText());
            if (!ValidationUtils.isValidSalary(salary)) {
                throw new IllegalArgumentException("Invalid salary.");
            }

            String email = emailField.getText();
            if (ValidationUtils.doesEmployeeExistByEmail(connection, email)) {
                showAlert("Error", "Employee with email " + email + " already exists.");
                return;
            }

            try {
                DatabaseConnection.insertEmployee(connection, name, departmentId, salary, email, this);
                navigateBackToDashboard();
            } catch (Exception e) {
                showAlert("Error", "Failed to add employee: " + e.getMessage());
            }
        });

        Scene addEmployeeScene = new Scene(grid, 400, 300);
        primaryStage.setScene(addEmployeeScene);
    }

    private void showAddDepartmentScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label departmentLabel = new Label("Department ID is added automatically");

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(departmentLabel, 0, 1);
        grid.add(submitButton, 1, 4);
        grid.add(backButton, 1, 5);

        backButton.setOnAction(event -> navigateBackToDashboard());

        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            if (!ValidationUtils.isValidName(name)) {
                throw new IllegalArgumentException("Invalid department name.");
            }
            try {
                DatabaseConnection.insertDepartment(connection, name, this);

                navigateBackToDashboard();
            } catch (Exception e) {
                showAlert("Error", "Failed to add department: " + e.getMessage());
            }
        });

        Scene addDepartmentScene = new Scene(grid, 400, 300);
        primaryStage.setScene(addDepartmentScene);
    }

    private void showEditEmployeeScene(Employee employee) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Employee ID:");
        TextField idField = new TextField(String.valueOf(employee.getId()));
        idField.setEditable(false);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(employee.getName());

        Label departmentLabel = new Label("Department ID:");
        TextField departmentField = new TextField(String.valueOf(employee.getDepartmentId()));

        Label salaryLabel = new Label("Salary:");
        TextField salaryField = new TextField(String.valueOf(employee.getSalary()));

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField(employee.getEmail());

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(departmentLabel, 0, 2);
        grid.add(departmentField, 1, 2);
        grid.add(salaryLabel, 0, 3);
        grid.add(salaryField, 1, 3);
        grid.add(emailLabel, 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(submitButton, 1, 5);
        grid.add(backButton, 1, 6);

        backButton.setOnAction(event -> UpdateEmployeeScene());

        submitButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                if (!ValidationUtils.isValidName(name)) {
                    throw new IllegalArgumentException("Invalid employee name.");
                }
                int departmentId = Integer.parseInt(departmentField.getText());
                if (!ValidationUtils.isValidId(departmentId)) {
                    showAlert("Error","Invalid department ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesDepartmentExist(connection, departmentId)) {
                    showAlert("Error","Department with ID " + departmentId + " does not exist.");
                    return;
                }
                double salary = Double.parseDouble(salaryField.getText());
                if (!ValidationUtils.isValidSalary(salary)) {
                    throw new IllegalArgumentException("Invalid salary.");
                }
                String email = emailField.getText();

                DatabaseConnection.updateEmployee(connection, employee.getId(), name, departmentId, salary, email);
                showAlert("Success", "Employee updated successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid input: Please ensure numeric fields are filled correctly.");
            }
        });

        Scene editEmployeeScene = new Scene(grid, 400, 400);
        primaryStage.setScene(editEmployeeScene);
    }


    private void UpdateEmployeeScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Button fetchButton = new Button("Fetch");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(fetchButton, 1, 1);

        backButton.setOnAction(event -> navigateBackToDashboard());
        fetchButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                runAsync(() -> {
                    try {
                        Employee employee = DatabaseConnection.getEmployeeById(connection, employeeId);
                        if (employee == null) {
                            Platform.runLater(() -> showAlert("Error", "Employee with ID " + employeeId + " not found."));
                        } else {
                            Platform.runLater(() -> showEditEmployeeScene(employee));
                        }
                    } catch (SQLException e) {
                        Platform.runLater(() -> showAlert("Error", "Failed to fetch employee: " + e.getMessage()));
                    }
                });
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ID: Please enter a numeric value.");
            }
        });

        Scene fetchEmployeeScene = new Scene(grid, 400, 200);
        primaryStage.setScene(fetchEmployeeScene);
    }

    private void showEditDepartmentScene(Department department) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Department ID:");
        TextField idField = new TextField(String.valueOf(department.getDepartmentId()));
        idField.setEditable(false);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(department.getName());

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(submitButton, 1, 5);
        grid.add(backButton, 1, 6);

        backButton.setOnAction(event -> UpdateDepartmentScene());

        submitButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                if (!ValidationUtils.isValidName(name)) {
                    throw new IllegalArgumentException("Invalid department name.");
                }

                DatabaseConnection.updateDepartment(connection, department.getDepartmentId(), name);
                showAlert("Success", "Department updated successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid input: Please ensure numeric fields are filled correctly.");
            }
        });

        Scene editDepartmentScene = new Scene(grid, 400, 400);
        primaryStage.setScene(editDepartmentScene);
    }

    private void UpdateDepartmentScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Department ID:");
        TextField idField = new TextField();
        Button fetchButton = new Button("Fetch");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(fetchButton, 1, 1);

        backButton.setOnAction(event -> navigateBackToDashboard());
        fetchButton.setOnAction(event -> {
            try {
                int departmentId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(departmentId)) {
                    showAlert("Error","Invalid department ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesDepartmentExist(connection, departmentId)) {
                    showAlert("Error", "Department with ID " + departmentId + " does not exist.");
                    return;
                }
                // Fetch employee details from the database
                Department department = DatabaseConnection.getDepartmentById(connection, departmentId);

                if (department == null) {
                    showAlert("Error", "Department with ID " + departmentId + " not found.");
                    return;
                }

                // Proceed to edit the fetched employee
                showEditDepartmentScene(department);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ID: Please enter a numeric value.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to fetch department: " + e.getMessage());
            }
        });

        Scene fetchDepartmentScene = new Scene(grid, 400, 200);
        primaryStage.setScene(fetchDepartmentScene);
    }


    private void DeleteEmployeeScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Button backButton = new Button("Back");
        Button deleteButton = new Button("Delete");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(backButton, 1, 1);
        grid.add(deleteButton, 1, 2);

        backButton.setOnAction(event -> navigateBackToDashboard());

        deleteButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(employeeId)) {
                    showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error", "Employee with ID " + employeeId + " does not exist.");
                    return;
                }
                DatabaseConnection.deleteEmployee(connection, employeeId);
                showAlert("Success", "Employee deleted successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ID: Please enter a numeric value.");
            }
        });

        Scene deleteEmployeeScene = new Scene(grid, 400, 200);
        primaryStage.setScene(deleteEmployeeScene);
    }

    private void DeleteDepartmentScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Department ID:");
        TextField idField = new TextField();
        Button backButton = new Button("Back");
        Button deleteButton = new Button("Delete");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(backButton, 1, 1);
        grid.add(deleteButton, 1, 2);

        backButton.setOnAction(event -> navigateBackToDashboard());

        deleteButton.setOnAction(event -> {
            try {
                int departmentId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(departmentId)) {
                    showAlert("Error","Invalid department ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesDepartmentExist(connection, departmentId)) {
                    showAlert("Error", "Department with ID " + departmentId + " does not exist.");
                    return;
                }
                DatabaseConnection.deleteDepartment(connection, departmentId);
                showAlert("Success", "Department deleted successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ID: Please enter a numeric value.");
            }
        });

        Scene deleteEmployeeScene = new Scene(grid, 400, 200);
        primaryStage.setScene(deleteEmployeeScene);
    }

    private void showViewAttendanceScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label label = new Label("Attendance List:");
        TextArea attendanceList = new TextArea();
        attendanceList.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigateBackToDashboard());

        try {
            StringBuilder attendances = new StringBuilder();
            DatabaseConnection.fetchAndDisplayAttendance(connection, attendances);
            attendanceList.setText(attendances.toString());
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch attendances: " + e.getMessage());
        }

        layout.getChildren().addAll(label, attendanceList, backButton);

        Scene viewAttendanceScene = new Scene(layout, 400, 400);
        primaryStage.setScene(viewAttendanceScene);
    }

    private void showViewPayrollScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label label = new Label("Payroll List:");
        TextArea payrollList = new TextArea();
        payrollList.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigateBackToDashboard());

        try {
            StringBuilder payrolls = new StringBuilder();
            DatabaseConnection.fetchAndDisplayPayroll(connection, payrolls);
            payrollList.setText(payrolls.toString());
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch payrolls: " + e.getMessage());
        }

        layout.getChildren().addAll(label, payrollList, backButton);

        Scene viewPayrollScene = new Scene(layout, 400, 400);
        primaryStage.setScene(viewPayrollScene);
    }

    private void showAddAttendanceScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label employeeLabel = new Label("Employee ID:");
        TextField employeeField = new TextField();
        Label dateLabel = new Label("Date (yyyy-mm-dd):");
        TextField dateField = new TextField();
        Label statusLabel = new Label("Status (Present/Absent):");
        TextField statusField = new TextField();

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(employeeLabel, 0, 0);
        grid.add(employeeField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(statusLabel, 0, 2);
        grid.add(statusField, 1, 2);
        grid.add(submitButton, 1, 4);
        grid.add(backButton, 1, 5);

        backButton.setOnAction(event -> navigateBackToDashboard());

        submitButton.setOnAction(event -> {
            int id = Integer.parseInt(employeeField.getText());
            if (!ValidationUtils.isValidId(id)) {
                showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                return;
            }
            if (!ValidationUtils.doesEmployeeExist(connection, id)) {
                showAlert("Error","Employee with ID " + id + " does not exist.");
                return;
            }
            String date = dateField.getText();
            if (!ValidationUtils.isValidDate(date) || !ValidationUtils.isDateInPastOrToday(date)) {
                throw new IllegalArgumentException("Invalid date format.");
            }
            if (ValidationUtils.doesAttendanceExist(connection, id, date)) {
                showAlert("Error"," Attendance record for Employee ID " + id + " on " + date + " already exist.");
                return;
            }
            String status = statusField.getText();
            if (!ValidationUtils.isValidAttendanceStatus(status)) {
                throw new IllegalArgumentException("Invalid attendance status.");
            }
            try {
                DatabaseConnection.insertAttendance(connection, id, date, status);
                showAlert("Success", "Employee added successfully!");
                navigateBackToDashboard();
            } catch (Exception e) {
                showAlert("Error", "Failed to add employee: " + e.getMessage());
            }
        });

        Scene addAttendanceScene = new Scene(grid, 400, 300);
        primaryStage.setScene(addAttendanceScene);
    }

    private void showAddPayrollScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label employeeLabel = new Label("Employee ID:");
        TextField employeeField = new TextField();
        Label workedLabel = new Label("Days Worked:");
        TextField workedField = new TextField();
        Label periodLabel = new Label("Pay period (YYYY-MM):");
        TextField periodField = new TextField();

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(employeeLabel, 0, 0);
        grid.add(employeeField, 1, 0);
        grid.add(workedLabel, 0, 1);
        grid.add(workedField, 1, 1);
        grid.add(periodLabel, 0, 2);
        grid.add(periodField, 1, 2);
        grid.add(submitButton, 1, 4);
        grid.add(backButton, 1, 5);

        backButton.setOnAction(event -> navigateBackToDashboard());

        submitButton.setOnAction(event -> {
            int id = Integer.parseInt(employeeField.getText());
            if (!ValidationUtils.isValidId(id)) {
                showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                return;
            }
            if (!ValidationUtils.doesEmployeeExist(connection, id)) {
                showAlert("Error","Employee with ID " + id + " does not exist.");
                return;
            }
            int workingDays = Integer.parseInt(workedField.getText());
            int daysWorked = ValidationUtils.validateWorkingDays(workingDays);
            String period = periodField.getText();
            if (!ValidationUtils.isValidPayPeriod(period)) {
                showAlert("Error","Invalid pay period. Format must be YYYY-MM.");
                return;
            }
            if (ValidationUtils.doesPayrollExist(connection, id, period)) {
                showAlert("Error","Payroll for Employee ID " + id + " in " + period + " already exists.");
                return;
            }
            try {
                DatabaseConnection.insertPayroll(connection, id, period, daysWorked);
                showAlert("Success", "Payroll added successfully!");
                navigateBackToDashboard();
            } catch (Exception e) {
                showAlert("Error", "Failed to add payroll: " + e.getMessage());
            }
        });

        Scene addPayrollScene = new Scene(grid, 400, 300);
        primaryStage.setScene(addPayrollScene);
    }

    private void UpdateAttendanceScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Label dateLabel = new Label("Enter date:");
        TextField dateField = new TextField();
        Button fetchButton = new Button("Fetch");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(fetchButton, 1, 2);

        backButton.setOnAction(event -> navigateBackToDashboard());
        fetchButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(employeeId)) {
                    showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error","Error: Employee with ID " + employeeId + " does not exist.");
                    return;
                }
                String date = dateField.getText();
                if (!ValidationUtils.isValidDate(date) || !ValidationUtils.isDateInPastOrToday(date)) {
                    throw new IllegalArgumentException("Invalid date format.");
                }
                if (!ValidationUtils.doesAttendanceExist(connection, employeeId, date)) {
                    showAlert("Error","Attendance record for Employee ID " + employeeId + " on " + date + " does not exist.");
                    return;
                }
                // Fetch employee details from the database
                Attendance attendance = DatabaseConnection.getAttendanceById(connection, employeeId, date);

                if (attendance == null) {
                    showAlert("Error", "Attendance with date " + date + " not found.");
                    return;
                }

                // Proceed to edit the fetched employee
                showEditAttendanceScene(attendance);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ID: Please enter a numeric value.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to fetch attendance: " + e.getMessage());
            }
        });

        Scene fetchAttendanceScene = new Scene(grid, 400, 200);
        primaryStage.setScene(fetchAttendanceScene);
    }

    private void showEditAttendanceScene(Attendance attendance) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label employeeLabel = new Label("Employee ID:");
        TextField employeeField = new TextField(String.valueOf(attendance.getEmployeeId()));
        employeeField.setEditable(false);

        Label dateLabel = new Label("Date:");
        TextField dateField = new TextField(attendance.getDate());
        dateField.setEditable(false);

        Label statusLabel = new Label("Status:");
        TextField statusField = new TextField(attendance.getStatus());

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(employeeLabel, 0, 1);
        grid.add(employeeField, 1, 1);
        grid.add(dateLabel, 0, 2);
        grid.add(dateField, 1, 2);
        grid.add(statusLabel, 0, 3);
        grid.add(statusField, 1, 3);
        grid.add(submitButton, 1, 5);
        grid.add(backButton, 1, 6);
        backButton.setOnAction(event -> UpdateAttendanceScene());
        submitButton.setOnAction(event -> {
            String newStatus = statusField.getText();
            if (!ValidationUtils.isValidAttendanceStatus(newStatus)) {
                throw new IllegalArgumentException("Invalid attendance status.");
            }
            DatabaseConnection.updateAttendance(connection, attendance.getEmployeeId(), attendance.getDate(), newStatus);
            showAlert("Success", "Attendance updated successfully!");
            navigateBackToDashboard();
        });

        Scene editAttendanceScene = new Scene(grid, 400, 400);
        primaryStage.setScene(editAttendanceScene);
    }

    private void UpdatePayrollScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Label periodLabel = new Label("Enter Pay Period (YYYY-MM):");
        TextField periodField = new TextField();
        Button fetchButton = new Button("Fetch");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(periodLabel, 0, 1);
        grid.add(periodField, 1, 1);
        grid.add(fetchButton, 1, 2);

        backButton.setOnAction(event -> navigateBackToDashboard());
        fetchButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(employeeId)) {
                    showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error","Employee with ID " + employeeId + " does not exist.");
                    return;
                }
                String period = periodField.getText();
                if (!ValidationUtils.isValidPayPeriod(period)) {
                    showAlert("Error","Invalid pay period. Format must be YYYY-MM.");
                    return;
                }
                if (!ValidationUtils.doesPayrollExist(connection, employeeId, period)) {
                    showAlert("Error","Payroll for Employee ID " + employeeId + " in " + period + " does not exist.");
                    return;
                }
                // Fetch payroll details from the database
                Payroll payroll = DatabaseConnection.getPayrollById(connection, employeeId, period);

                if (payroll == null) {
                    showAlert("Error", "No payroll record found for Employee ID " + employeeId + " for the period " + period + ".");
                    return;
                }

                // Proceed to edit the fetched payroll record
                showEditPayrollScene(payroll);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid Employee ID: Please enter a numeric value.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to fetch payroll: " + e.getMessage());
            }
        });

        Scene fetchPayrollScene = new Scene(grid, 400, 200);
        primaryStage.setScene(fetchPayrollScene);
    }

    private void showEditPayrollScene(Payroll payroll) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Employee ID:");
        TextField idField = new TextField(String.valueOf(payroll.getEmployeeId()));
        idField.setEditable(false);

        Label periodLabel = new Label("Pay Period:");
        TextField periodField = new TextField(payroll.getPayPeriod());
        periodField.setEditable(false);

        Label daysWorkedLabel = new Label("Days Worked:");
        TextField daysWorkedField = new TextField(String.valueOf(payroll.getDaysWorked()));

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(periodLabel, 0, 1);
        grid.add(periodField, 1, 1);
        grid.add(daysWorkedLabel, 0, 2);
        grid.add(daysWorkedField, 1, 2);
        grid.add(submitButton, 1, 4);
        grid.add(backButton, 1, 5);

        backButton.setOnAction(event -> UpdatePayrollScene());

        submitButton.setOnAction(event -> {
            try {
                int daysWorked = Integer.parseInt(daysWorkedField.getText());
                int newDaysWorked = ValidationUtils.validateWorkingDays(daysWorked);
                // Update payroll in the database
                DatabaseConnection.updatePayroll(connection, payroll.getEmployeeId(), payroll.getPayPeriod(), newDaysWorked);
                showAlert("Success", "Payroll updated successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid input: Days Worked must be numeric.");
            }
        });

        Scene editPayrollScene = new Scene(grid, 400, 300);
        primaryStage.setScene(editPayrollScene);
    }

    private void DeleteAttendanceScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Label dateLabel = new Label("Enter Date (yyyy-mm-dd):");
        TextField dateField = new TextField();

        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(deleteButton, 1, 2);
        grid.add(backButton, 1, 3);

        backButton.setOnAction(event -> navigateBackToDashboard());

        deleteButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(employeeId)) {
                    showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error","Employee with ID " + employeeId + " does not exist.");
                    return;
                }
                String date = dateField.getText();
                if (!ValidationUtils.isValidDate(date) || !ValidationUtils.isDateInPastOrToday(date)) {
                    throw new IllegalArgumentException("Invalid date format.");
                }
                if (!ValidationUtils.doesAttendanceExist(connection, employeeId, date)) {
                    showAlert("Error","Attendance record for Employee ID " + employeeId + " on " + date + " does not exist.");
                    return;
                }
                // Delete attendance record from the database
                DatabaseConnection.deleteAttendance(connection, employeeId, date);
                showAlert("Success", "Attendance deleted successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid Employee ID: Please enter a numeric value.");
            }
        });

        Scene deleteAttendanceScene = new Scene(grid, 400, 200);
        primaryStage.setScene(deleteAttendanceScene);
    }

    private void DeletePayrollScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Enter Employee ID:");
        TextField idField = new TextField();
        Label periodLabel = new Label("Enter Pay Period (YYYY-MM):");
        TextField periodField = new TextField();

        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(periodLabel, 0, 1);
        grid.add(periodField, 1, 1);
        grid.add(deleteButton, 1, 2);
        grid.add(backButton, 1, 3);

        backButton.setOnAction(event -> navigateBackToDashboard());

        deleteButton.setOnAction(event -> {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                if (!ValidationUtils.isValidId(employeeId)) {
                    showAlert("Error","Invalid employee ID. Please enter a positive integer.");
                    return;
                }
                if (!ValidationUtils.doesEmployeeExist(connection, employeeId)) {
                    showAlert("Error","Employee with ID " + employeeId + " does not exist.");
                    return;
                }
                String period = periodField.getText();
                if (!ValidationUtils.isValidPayPeriod(period)) {
                    showAlert("Error","Invalid pay period. Format must be YYYY-MM.");
                    return;
                }
                if (!ValidationUtils.doesPayrollExist(connection, employeeId, period)) {
                    showAlert("Error","Payroll for Employee ID " + employeeId + " in " + period + " does not exist.");
                    return;
                }
                // Delete payroll record from the database
                DatabaseConnection.deletePayroll(connection, employeeId, period);
                showAlert("Success", "Payroll record deleted successfully!");
                navigateBackToDashboard();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid Employee ID: Please enter a numeric value.");
            }
        });

        Scene deletePayrollScene = new Scene(grid, 400, 200);
        primaryStage.setScene(deletePayrollScene);
    }


    public boolean authenticate(String username, String password) {
        try {
            switch (username) {
                case "admin":
                    connection = DatabaseConnection.getConnection("admin");
                    return password.equals("admin_password");
                case "manager":
                    connection = DatabaseConnection.getConnection("manager");
                    return password.equals("manager_password");
                case "employee":
                    connection = DatabaseConnection.getConnection("employee");
                    return password.equals("employee_password");
                default:
                    return false;
            }
        } catch (SQLException e) {
            showAlert("Error", "Database connection failed: " + e.getMessage());
            return false;
        }
    }

    private String determineRole(String username) {
        return switch (username) {
            case "admin" -> "admin";
            case "manager" -> "manager";
            case "employee" -> "employee";
            default -> null;
        };
    }

    private void navigateBackToDashboard() {
        switch (currentUserRole) {
            case "admin" -> showAdminDashboard();
            case "manager" -> showManagerDashboard();
            case "employee" -> showEmployeeDashboard();
            default -> showLoginScene(); // Fallback to login if the role is invalid
        }
    }

    public void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private static void runTests() {
        System.out.println("Running tests...");

        EmployeeManagementScenesTest testSuite = new EmployeeManagementScenesTest();

        try {
            testSuite.testConstructorDatabaseConnection();
            System.out.println("testConstructorDatabaseConnection passed.");
            testSuite.testDatabaseConnectionForAdmin();
            System.out.println("testDatabaseConnectionForAdmin passed.");
            testSuite.testAuthenticateAdminSuccess();
            System.out.println("testAuthenticateAdminSuccess passed.");

            testSuite.testAuthenticateManagerFailure();
            System.out.println("testAuthenticateManagerFailure passed.");

            testSuite.testAuthenticateEmployeeSuccess();
            System.out.println("testAuthenticateEmployeeSuccess passed.");

            testSuite.testAuthenticateInvalidUser();
            System.out.println("testAuthenticateInvalidUser passed.");

            System.out.println("All tests passed successfully!");
        } catch (AssertionError e) {
            System.err.println("A test failed: " + e.getMessage());
            System.exit(1);
        }
    }

}
