package com.inventory.management.ui;

import com.inventory.management.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private User adminUser = new User("admin", "admin123", User.Role.ADMIN);
    private User staffUser = new User("staff", "staff123", User.Role.STAFF);

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            loadMainUI();
        } else {
            showAlert("Login Failed", "The username or password you entered is incorrect.");
        }
    }

    private boolean authenticate(String username, String password) {
        if (username.equals(adminUser.getUsername()) && adminUser.checkPassword(password)) {
            UIManager.currentUser = adminUser;
            return true;
        } else if (username.equals(staffUser.getUsername()) && staffUser.checkPassword(password)) {
            UIManager.currentUser = staffUser;
            return true;
        }
        return false;
    }

    private void loadMainUI() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/main.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Inventory Management System - Main");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the main interface.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert;
        if ("Login Failed".equalsIgnoreCase(title) || "Error".equalsIgnoreCase(title)) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
