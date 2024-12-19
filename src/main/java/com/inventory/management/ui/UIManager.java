package com.inventory.management.ui;

import com.inventory.management.model.Product;
import com.inventory.management.model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Manages the application's UI scenes and holds global data like inventory and current user.
 */
public class UIManager extends Application {

    public static ObservableList<Product> inventory = FXCollections.observableArrayList();
    public static User currentUser = null; // Store user after login
    public static Stage primaryStage; // Reference to the primary stage

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        try {
            initializeSampleData(); // Initialize inventory BEFORE loading scenes

            // Load Login Screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Inventory Management System - Login");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the UI: " + e.getMessage());
        }
    }

    /**
     * Initializes sample inventory data for demonstration purposes.
     */
    private void initializeSampleData() {
        inventory.add(new Product("P001", "Apple", 2.50, 100, "Fruits", "/images/apple.png", LocalDate.now().minusDays(2)));
        inventory.add(new Product("P002", "Banana", 1.20, 200, "Fruits", "/images/banana.png", LocalDate.now().minusDays(1)));
        inventory.add(new Product("P003", "Orange", 2.00, 150, "Fruits", "/images/orange.png", LocalDate.now().minusDays(3)));
        inventory.add(new Product("P010", "Laptop", 1000.00, 20, "Electronics", "/images/laptop.png", LocalDate.now().minusDays(10)));
        inventory.add(new Product("P011", "Smartphone", 800.00, 50, "Electronics", "/images/smartphone.png", LocalDate.now().minusDays(5)));
        inventory.add(new Product("P012", "Headphones", 150.00, 80, "Accessories", "/images/headphones.png", LocalDate.now().minusDays(7)));
        inventory.add(new Product("P013", "Keyboard", 70.00, 60, "Accessories", "/images/keyboard.png", LocalDate.now().minusDays(4)));
    }

    /**
     * Loads the Login scene.
     */
    public static void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load Login scene: " + e.getMessage());
        }
    }

    /**
     * Loads the Main scene.
     */
    public static void loadMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/main.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Main");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load Main scene: " + e.getMessage());
        }
    }

    /**
     * Loads the Chart scene.
     */
    public static void loadChartScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/chart.fxml"));
            Scene scene = new Scene(loader.load());
            Stage chartStage = new Stage();
            chartStage.setTitle("Inventory Management System - Charts");
            chartStage.setScene(scene);
            chartStage.initModality(Modality.APPLICATION_MODAL);
            chartStage.initOwner(primaryStage);
            chartStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load Chart scene: " + e.getMessage());
        }
    }

    /**
     * Loads the Report scene.
     */
    public static void loadReportScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/report.fxml"));
            Scene scene = new Scene(loader.load());
            Stage reportStage = new Stage();
            reportStage.setTitle("Inventory Management System - Reports");
            reportStage.setScene(scene);
            reportStage.initModality(Modality.APPLICATION_MODAL);
            reportStage.initOwner(primaryStage);
            reportStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load Report scene: " + e.getMessage());
        }
    }

    /**
     * Launches the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
