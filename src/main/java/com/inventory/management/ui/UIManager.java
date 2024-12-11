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
    public static Stage currentStage; // Add this line

    @Override
    public void start(Stage primaryStage) {
        currentStage = primaryStage; // Assign primaryStage to currentStage
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

            initializeSampleData();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the UI: " + e.getMessage());
        }
    }

    private void initializeSampleData() {
        // Assuming the sixth parameter is imagePath. Update accordingly based on your Product class.
        inventory.add(new Product("P001", "Apple", 2.50, 100, "Fruits", "/images/apple.png"));
        inventory.add(new Product("P002", "Banana", 1.20, 200, "Fruits", "/images/banana.png"));
        inventory.add(new Product("P003", "Orange", 2.00, 150, "Fruits", "/images/orange.png"));
        inventory.add(new Product("P010", "Laptop", 1000.00, 20, "Electronics", "/images/laptop.png"));
        inventory.add(new Product("P011", "Smartphone", 800.00, 50, "Electronics", "/images/smartphone.png"));

        // If specific images are not available, you can use a default image path or an empty string
        // inventory.add(new Product("P001", "Apple", 2.50, 100, "Fruits", "/images/default_product.png"));
    }

    public static void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setTitle("Inventory Management System - Login");
            currentStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadChartScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/chart.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setTitle("Charts");
            currentStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadHomeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/main.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setTitle("Inventory Management System - Home");
            currentStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
