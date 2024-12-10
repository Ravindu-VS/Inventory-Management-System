package com.inventory.management.ui;

import com.inventory.management.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Hardcoded for demonstration
    private User adminUser = new User("admin", "admin123", "Admin");
    private User staffUser = new User("staff", "staff123", "Staff");

    public void onLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.equals(adminUser.getUsername()) && adminUser.checkPassword(pass)) {
            UIManager.currentUser = adminUser;
            loadMainUI();
        } else if (user.equals(staffUser.getUsername()) && staffUser.checkPassword(pass)) {
            UIManager.currentUser = staffUser;
            loadMainUI();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid Credentials");
            alert.show();
        }
    }

    private void loadMainUI() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/main.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Inventory Management System - Main");
            stage.setScene(scene);
            stage.show(); // Ensure the new scene is displayed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
