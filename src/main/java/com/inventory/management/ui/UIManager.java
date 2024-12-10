package com.inventory.management.ui;

import com.inventory.management.model.Product;
import com.inventory.management.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UIManager extends Application {

    public static List<Product> inventory = new ArrayList<>();
    public static User currentUser = null; // Store user after login

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load Login Screen
            URL fxmlLocation = getClass().getResource("/com/inventory/management/ui/views/login.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("Cannot find login.fxml in /com/inventory/management/ui/views/");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Initialize sample data
            initializeSampleData();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the UI: " + e.getMessage());
        }
    }

    private void initializeSampleData() {
        inventory.add(new Product("P001", "Apple", 2.50, 100, "Fruits"));
        inventory.add(new Product("P002", "Banana", 1.20, 200, "Fruits"));
        inventory.add(new Product("P003", "Orange", 2.00, 150, "Fruits"));
        inventory.add(new Product("P010", "Laptop", 1000.00, 20, "Electronics"));
        inventory.add(new Product("P011", "Smartphone", 800.00, 50, "Electronics"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
