// src/main/java/com/inventory/management/ui/UIManager.java
package com.inventory.management.ui;

import com.inventory.management.db.MongoDBClient;
import com.inventory.management.model.Product;
import com.inventory.management.model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class UIManager extends Application {

    public static ObservableList<Product> inventory = FXCollections.observableArrayList();
    public static User currentUser = null;
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        MongoDBClient.connect();
        List<Product> dbProducts = MongoDBClient.loadProducts();
        inventory.setAll(dbProducts);

        // Add sample products if inventory is empty
        if (inventory.isEmpty()) {
            Product p1 = new Product("P001", "Apple", 2.50, 100, "Fruits", "/images/apple.png", LocalDate.now());
            Product p2 = new Product("P002", "Laptop", 1000.00, 10, "Electronics", "/images/laptop.png", LocalDate.now());
            Product p3 = new Product("P003", "Banana", 1.20, 200, "Fruits", "/images/banana.png", LocalDate.now());

            inventory.addAll(p1, p2, p3);
            MongoDBClient.addOrUpdateProduct(p1);
            MongoDBClient.addOrUpdateProduct(p2);
            MongoDBClient.addOrUpdateProduct(p3);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Inventory Management System - Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the login UI.");
        }
    }

    public static void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/main.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Main");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveProductToDB(Product product) {
        MongoDBClient.addOrUpdateProduct(product);
    }

    public static void deleteProductFromDB(Product product) {
        MongoDBClient.deleteProduct(product.getProductId());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
